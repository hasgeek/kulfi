package com.hasgeek.zalebi.network;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by heisenberg on 10/07/15.
 */
public class AuthHelper {
    private static String SHARED_PREF_NAME = "auth";
    private static String ACCESS_TOKEN_KEY = "access_token";

    Context mContext;
    SharedPreferences mSharedPreferences;

    public AuthHelper(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveAccessToken(String accessToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.apply();
    }

    public String getAccessToken(){
        return mSharedPreferences.getString(ACCESS_TOKEN_KEY, "");
    }

    public void invalidateSession() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, "");
        editor.apply();
    }

    public boolean isLoggedIn(){
        return !mSharedPreferences.getString(ACCESS_TOKEN_KEY, "").isEmpty();
    }
}
