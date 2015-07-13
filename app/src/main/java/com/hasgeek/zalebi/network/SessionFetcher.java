package com.hasgeek.zalebi.network;

import android.content.Context;
import android.os.Environment;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;


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

    public void fetch()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://metarefresh.talkfunnel.com/2015/json", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Session> sessionList = new ArrayList<>();
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/hasgeek");
                    dir.mkdir();
                    FileWriter fileWriter = new FileWriter(new File(dir.getAbsolutePath() + "/sessions.json"));
                    Log.d("hasgeek","Written the sessions JSON successfully*****");
                    fileWriter.write(response.toString());
                    fileWriter.close();
                } catch (Exception e) {
                    Log.e("hasgeek","Exception raised!!!!"+e.getMessage());
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
