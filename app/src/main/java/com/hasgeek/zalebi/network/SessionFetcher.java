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
import com.hasgeek.zalebi.model.Session;

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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://metarefresh.talkfunnel.com/2015/json", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
//                    JSONObject sessionsJSON = new JSONObject(response.toString());
                    JSONArray schedules = json.getJSONArray("schedule");
                    Log.d("hasgeek", "number of schedules " + schedules.length());
                    String spaceId = json.getJSONObject("space").getString("id");
                    Log.d("hasgeek", "spaceId " + spaceId);

                    Gson gson = new Gson();
                    Session.deleteAll(Session.class,"space_id = ?",spaceId);
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
                    }
                    Session.saveInTx(sessions);
                    Log.d("hasgeek", "number of sessions inserted for space ID " + sessions.size());
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
