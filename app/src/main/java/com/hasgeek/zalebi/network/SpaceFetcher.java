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

public class SpaceFetcher {

    private Context context;
    private RequestQueue requestQueue;

    public SpaceFetcher(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void fetch() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Config.SPACES_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    Gson gson = new Gson();
                    List<Space> spaces = new ArrayList<>();
                    spaces.addAll(Arrays.asList(gson.fromJson(json.optString("spaces"),
                            Space[].class)));
                    Space space;
                    for(int i = 0; i < Config.MAX_NUM_OF_SPACES;i++){
                        space = spaces.get(i);
                        space.saveOrUpdate();
                    }
                    Log.d("hasgeek", "number of spaces read from JSON " + spaces.size());
                    Log.d("hasgeek","number of spaces inserted "+Space.listAll(Space.class).size());
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

