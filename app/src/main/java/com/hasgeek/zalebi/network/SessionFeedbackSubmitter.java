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
import com.hasgeek.zalebi.model.SessionFeedback;
import com.hasgeek.zalebi.model.Space;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karthikbalakrishnan on 08/09/15.
 */
public class SessionFeedbackSubmitter {

    Context context;
    RequestQueue requestQueue;
    SessionFeedbackSubmitListener mSessionFeedbackSubmitListener;

    public interface SessionFeedbackSubmitListener {
        void onSessionFeedbackSubmitSuccess(SessionFeedback sessionFeedback);
        void onSessionFeedbackSubmitFailure();
    }

    public SessionFeedbackSubmitter(Context context, SessionFeedbackSubmitListener sessionFeedbackSubmitListener) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        mSessionFeedbackSubmitListener = sessionFeedbackSubmitListener;
    }

    public void submit(final SessionFeedback sessionFeedback){
        String url = ""; //TODO construct URL for feedback submission
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {

                mSessionFeedbackSubmitListener.onSessionFeedbackSubmitSuccess(sessionFeedback);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSessionFeedbackSubmitListener.onSessionFeedbackSubmitFailure();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
