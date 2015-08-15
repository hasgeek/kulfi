package com.hasgeek.zalebi.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SessionFetcher {
    Context context;
    RequestQueue requestQueue;

    public SessionFetcher(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void fetch(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Config.CONF_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    JSONArray schedules = json.getJSONArray("schedule");
                    Log.d("hasgeek", "number of schedules " + schedules.length());
                    String spaceId = json.getJSONObject("space").getString("id");
                    Log.d("hasgeek", "spaceId " + spaceId);

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    List<Session> sessions = new ArrayList<>();
                    for (int i = 0; i < schedules.length(); i++) {
                        JSONObject schedule = (JSONObject) schedules.get(i);
                        JSONArray slots = schedule.getJSONArray("slots");
                        for (int j = 0; j < slots.length(); j++) {
                            JSONObject slot = (JSONObject) slots.get(j);
                            sessions.addAll(Arrays.asList(gson.fromJson(slot.getString("sessions"),
                                    Session[].class)));

                            Log.d("talkfunnel", "added sessions for slot " + slot.getString("slot"));
                        }
                    }
                    for(Session session:sessions){
                        session.setSpaceId(spaceId);
//                        session.setSessionId(session.getId());
//                        session.setId(null);
//                        session.save();
                        session.saveOrUpdate();
                        Log.d("hasgeek","session id "+session.getSessionId()+" id "+session.getId());
                    }
                    Log.d("hasgeek", "number of sessions inserted for space ID " + sessions.size());
//                    Log.d("hasgeek","number of sessions retrieved from DB "+Session.listAll(Session.class).size());
                } catch (Exception e) {
                    Log.e("hasgeek","Exception raised!!!!"+e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
