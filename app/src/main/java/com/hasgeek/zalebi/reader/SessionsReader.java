package com.hasgeek.zalebi.reader;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.activity.TalkFunnelActivity;
import com.hasgeek.zalebi.model.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionsReader {

    private final Context context;
    private final SessionReadListener listener;

    public SessionsReader(Context context,SessionReadListener sessionReadListener){
        this.context = context;
        this.listener = sessionReadListener;
    }

    public List<Session> readSessions(){
        List<Session> sessions = new ArrayList<>();
        try {
            sessions = Session.find(Session.class,"space_id = ?", TalkFunnelActivity.SPACE_ID);
//            sessions = Session.listAll(Session.class);

            listener.onSessionReadSuccess(sessions);
        }
        catch (Exception ex){
            listener.onSessionReadFailure();
            Log.e("hasgeek",
                    "Exception while reading sessions  "+ex.getMessage());
        }
        return sessions;
    }

    public interface SessionReadListener {
        public void onSessionReadSuccess(List<Session> sessions);
        public void onSessionReadFailure();
    }

}
