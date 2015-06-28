package com.hasgeek.zalebi.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.util.TimeUtils;

import java.util.List;

/**
 * Created by heisenberg on 05/06/15.
 */
public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SessionsViewHolder>{
    private List<Session> mSessions;

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
