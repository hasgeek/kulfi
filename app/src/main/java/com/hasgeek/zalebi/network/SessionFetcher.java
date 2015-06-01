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
import com.google.gson.JsonObject;
import com.hasgeek.zalebi.model.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by heisenberg on 29/05/15.
 */
public class SessionFetcher {
    Context context;
    RequestQueue requestQueue;
    SessionFetchListener mSessionFetchListener;

    public interface SessionFetchListener{
        void onSessionFetchSuccess(ArrayList<Session> sessions);
        void onSessionFetchFailure();
    }

    public SessionFetcher(Context context, SessionFetchListener sessionFetchListener) {
        this.context = context;
        mSessionFetchListener = sessionFetchListener;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void fetch(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://metarefresh.talkfunnel.com/2015/json", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Session> sessionList = new ArrayList<>();
                    Gson gson = new Gson();
                    JSONArray schedules = response.getJSONArray("schedule");
                    for(int i=0; i<schedules.length(); i++){
                        JSONObject schedule = (JSONObject) schedules.get(i);
                        JSONArray slots = schedule.getJSONArray("slots");
                        //sessionList.addAll(Arrays.asList(gson.fromJson(slots.getJSONArray("sessions").toString(), Session[].class)));
                        for(int j = 0; j<slots.length(); j++){
                            JSONObject slot = (JSONObject) slots.get(j);
                            sessionList.addAll(Arrays.asList(gson.fromJson(slot.getString("sessions"), Session[].class)));
                            Log.d("talkfunnel","added sessions for slot "+slot.getString("slot"));
                        }
                    }
                    mSessionFetchListener.onSessionFetchSuccess(sessionList);
                } catch (JSONException e) {
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

    private Session constructSession(JSONObject jsonObject){
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.toString(), Session.class);
    }

}
