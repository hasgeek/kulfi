package com.hasgeek.zalebi.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasgeek.zalebi.model.Contact;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by heisenberg on 12/08/15.
 */
public class ContactFetcher {
    Context mContext;
    RequestQueue mRequestQueue;
    ContactFetchListener mContactFetchListener;

    public interface ContactFetchListener{
        public void onContactFetchSuccess(Contact contact);
        public void onContactFetchFailure();
    }

    public ContactFetcher(Context context, ContactFetchListener contactFetchListener) {
        mContext = context;
        mContactFetchListener = contactFetchListener;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public void fetch(String participantUrl){
        GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET, participantUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Contact contact = gson.fromJson(response.optString("participant", "{}"), Contact.class);
                List<Contact> persistedContacts = Contact.find(Contact.class, "user_id = ?", contact.getUserId());
                if (persistedContacts.isEmpty()) {
                    contact.save();
                } else {
                    for (Contact persistedContact : persistedContacts) {
                        persistedContact.delete();
                    }
                    contact.save();
                }
                mContactFetchListener.onContactFetchSuccess(contact);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mContactFetchListener.onContactFetchFailure();
            }
        }, mContext);
        mRequestQueue.add(customJsonObjectRequest);
    }
}
