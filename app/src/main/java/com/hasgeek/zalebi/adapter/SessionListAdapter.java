package com.hasgeek.zalebi.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SessionsViewHolder>{
    private  Context context;
    private List<Session> mSessions = new ArrayList<>();

    public SessionListAdapter(Context context){
        this.context = context;
        Gson gson = new Gson();
        try{
            File sessionListJSONFile = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath()+"/hasgeek/sessions.json");

            StringBuilder json = new StringBuilder("");
            InputStream fileStream;
            if(sessionListJSONFile.exists()){
                Log.d("hasgeek","found file in SD card");
                fileStream = new FileInputStream(sessionListJSONFile);
            }
            else{
                fileStream = context.getResources().openRawResource(R.raw.sessions);
            }

            BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream));
            String line = "";
            while ((line = fileReader.readLine()) != null){
                json.append(line);
            }
            JSONObject sessionsJSON = new JSONObject(json.toString());
            JSONArray schedules = sessionsJSON.getJSONArray("schedule");
            Log.d("hasgeek","number of schedules "+schedules.length());
            for(int i=0; i<schedules.length(); i++){
                JSONObject schedule = (JSONObject) schedules.get(i);
                JSONArray slots = schedule.getJSONArray("slots");
                for(int j = 0; j<slots.length(); j++){
                    JSONObject slot = (JSONObject) slots.get(j);
                    mSessions.addAll(Arrays.asList(gson.fromJson(slot.getString("sessions"), Session[].class)));
                    Log.d("talkfunnel","added sessions for slot "+slot.getString("slot"));
                }
            }

        }
        catch (Exception ex){
            Log.e("hasgeek","Exception while reading sessions "+ex.getMessage());
        }

    }
    public SessionListAdapter(List<Session> sessions) {
        mSessions = sessions;
    }

    public static class SessionsViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        protected TextView mSessionTitle;
        protected TextView mSpeakerName;
        protected TextView mStartTime;
        protected TextView mDuration;

        public SessionsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mSessionTitle = (TextView) view.findViewById(R.id.session_title);
            mSpeakerName = (TextView) view.findViewById(R.id.speaker_name);
            mStartTime = (TextView) view.findViewById(R.id.start_time);
            mDuration = (TextView) view.findViewById(R.id.session_duration);
        }

        @Override
        public void onClick(View v) {
            TextView sessionTitle = (TextView) v.findViewById(R.id.session_title);
            Log.e("funnel", "click..." + sessionTitle.getText());
        }
    }

    @Override
    public SessionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_list_item, parent, false);
        return new SessionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionsViewHolder holder, int position) {
        Session session = mSessions.get(position);
        holder.mSessionTitle.setText(session.getTitle());
        holder.mSpeakerName.setText(session.getSpeaker());
        holder.mStartTime.setText(TimeUtils.displayableTime(session.getStart()));
        holder.mDuration.setText(TimeUtils.getDuration(session.getStart(), session.getEnd()));
    }

    @Override
    public int getItemCount() {
        return mSessions.size();
    }

}
