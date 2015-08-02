package com.hasgeek.zalebi.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heisenberg on 10/07/15.
 */
public class CustomJsonObjectRequest extends JsonObjectRequest{
    Context mContext;
    AuthHelper mAuthHelper;

    interface HEADER {
        String AUTHORIZATION = "Authorization";
    }

    public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, jsonRequest, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(15000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mContext = context;
        mAuthHelper = new AuthHelper(mContext);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HEADER.AUTHORIZATION, "Bearer " + mAuthHelper.getAccessToken());
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        NetworkResponse networkResponse = volleyError.networkResponse;
        if (networkResponse != null && (networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED)) {
            mAuthHelper.invalidateSession();
        }
        return super.parseNetworkError(volleyError);
    }
}
