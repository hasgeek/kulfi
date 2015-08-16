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
import com.hasgeek.zalebi.model.Space;
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

    public void syncSessions(){
        List<Space> spaces = Space.listAll(Space.class);
        for(Space space : spaces){
            Log.d("hasgeek","Syncing sessions for space "+space.getTitle());
            fetch(space.getJsonURL());
        }
    }

    public void fetch(String url){
        Log.d("hasgeek","fetching sessions for url "+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    JSONArray schedules = json.getJSONArray("schedule");
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

                        }
                    }
                    for(Session session:sessions){
                        session.setSpaceId(spaceId);
                        session.saveOrUpdate();
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
