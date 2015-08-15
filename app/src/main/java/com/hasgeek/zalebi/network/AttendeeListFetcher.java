package com.hasgeek.zalebi.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasgeek.zalebi.model.Attendee;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by heisenberg on 09/08/15.
 */
public class AttendeeListFetcher {
    Context mContext;
    RequestQueue requestQueue;

    public AttendeeListFetcher(Context context) {
        mContext = context;
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public void fetch() {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET,
                "https://rootconf.talkfunnel.com/2015/participants/json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                new ScheduledThreadPoolExecutor(1).execute(new Runnable() {
                    @Override
                    public void run() {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        final List<Attendee> attendees = Arrays.asList(gson.fromJson(
                                response.optString("participants", "{}"), Attendee[].class));
                        for (Attendee attendee : attendees) {
                            List<Attendee> persistedAttendees = Attendee.find(Attendee.class, "user_id = ?", attendee.getUserId());
                            if (persistedAttendees.isEmpty()) {
                                attendee.save();
                            } else {
                                for (Attendee persistedAttendee : persistedAttendees) {
                                    //TODO: Get rid of blind update
                                    persistedAttendee.updateProperties(attendee);
                                    persistedAttendee.save();
                                }
                            }
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("funnel", "error " + error.getMessage());
            }
        }, mContext);
        requestQueue.add(customJsonObjectRequest);
    }
}
