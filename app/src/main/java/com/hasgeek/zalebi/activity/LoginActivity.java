package com.hasgeek.zalebi.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.network.AttendeeListFetcher;
import com.hasgeek.zalebi.network.AuthHelper;
import com.hasgeek.zalebi.network.CustomJsonObjectRequest;

import org.json.JSONObject;

import java.net.URI;
import java.util.List;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AuthHelper authHelper = new AuthHelper(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Uri data = getIntent().getData();
        Uri uri = Uri.parse("http://hasgeek.com/?" + data.getFragment());
        final String access_token = uri.getQueryParameter("access_token");
        authHelper.saveAccessToken(access_token);
        Log.d("hasgeek", "logged in successfully");
        new AttendeeListFetcher(LoginActivity.this).syncAttendees();
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET,
                "https://talkfunnel.com/api/whoami",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.optInt("code", -1) != 200){

                        }
                        Toast.makeText(LoginActivity.this, response.optString("message", "Successfully Logged in!"), Toast.LENGTH_LONG).show();
                        Intent intent =  new Intent(LoginActivity.this, TalkFunnelActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        , this);
        requestQueue.add(customJsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
