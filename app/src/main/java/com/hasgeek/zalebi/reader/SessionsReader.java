package com.hasgeek.zalebi.reader;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.model.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class SessionsReader {

    private final Context context;
    private final SessionReadListener listener;
    private int resourceId;

    public SessionsReader(Context context,SessionReadListener sessionReadListener){
        this.context = context;
        this.listener = sessionReadListener;
        resourceId = R.raw.sessions;
    }

    public ArrayList<Session> readSessions(){
        ArrayList<Session> sessions = new ArrayList<>();
        try {
            BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(context.getResources().
                            openRawResource(resourceId)));
            String line = "";
            StringBuilder json = new StringBuilder("");
            while ((line = fileReader.readLine()) != null) {
                json.append(line);
            }
            JSONObject sessionsJSON = new JSONObject(json.toString());
            JSONArray schedules = sessionsJSON.getJSONArray("schedule");
            Log.d("hasgeek", "number of schedules " + schedules.length());
            Gson gson = new Gson();
            for (int i = 0; i < schedules.length(); i++) {
                JSONObject schedule = (JSONObject) schedules.get(i);
                JSONArray slots = schedule.getJSONArray("slots");
                for (int j = 0; j < slots.length(); j++) {
                    JSONObject slot = (JSONObject) slots.get(j);
                    sessions.addAll(
                            Arrays.asList(gson.fromJson(slot.getString("sessions"),
                            Session[].class)));
                    Log.d("talkfunnel", "added sessions for slot " + slot.getString("slot"));
                }
            }
            listener.onSessionReadSuccess(sessions);
        }
        catch (Exception ex){
            listener.onSessionReadFailure();
            Log.e("hasgeek",
                    "Exception while reading sessions from the Resource file "+ex.getMessage());
        }
        return sessions;
    }

    public interface SessionReadListener {
        public void onSessionReadSuccess(ArrayList<Session> sessions);
        public void onSessionReadFailure();
    }

    void setResourceId(int resourceId){
        this.resourceId = resourceId;
    }

}
