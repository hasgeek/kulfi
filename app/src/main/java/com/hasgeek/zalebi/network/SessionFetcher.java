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

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;


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
            public void onResponse(JSONObject response) {
                try {
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/hasgeek");
                    dir.mkdir();
                    FileWriter fileWriter = new FileWriter(new File(dir.getAbsolutePath() + "/sessions.json"));
                    Log.e("hasgeek","Written the sessions JSON successfully*****");
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
}
