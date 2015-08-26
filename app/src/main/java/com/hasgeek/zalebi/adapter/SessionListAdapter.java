package com.hasgeek.zalebi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.activity.SessionActivity;
import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.util.DateTimeUtils;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import java.util.List;

/**
 * Created by heisenberg on 05/06/15.
 */
public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SessionsViewHolder> {
    private List<Session> mSessions;

    private static final int VIEW_TYPE_HEADER = 0x01;
    private static final int VIEW_TYPE_CONTENT = 0x00;

    public SessionListAdapter(List<Session> sessions) {
        mSessions = sessions;
    }

    public static class SessionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView mSessionTitle;
        protected TextView mSpeakerName;
        protected TextView mStartTime;
        protected TextView mDuration;
        protected Session mSession;
        protected View mView;

        public SessionsViewHolder(View view) {
            super(view);
            mView = view;
            view.setOnClickListener(this);
            mSessionTitle = (TextView) view.findViewById(R.id.session_title);
            mSpeakerName = (TextView) view.findViewById(R.id.speaker_name);
            mStartTime = (TextView) view.findViewById(R.id.start_time);
            mDuration = (TextView) view.findViewById(R.id.session_duration);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            TextView sessionTitle = (TextView) v.findViewById(R.id.session_title);

            Bundle bundle = new Bundle();
            bundle.putParcelable("session", mSession);
            Intent intent = new Intent(context, SessionActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @Override
    public SessionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_list_item, parent, false);
        }
        return new SessionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionsViewHolder holder, int position) {
        Session session = mSessions.get(position);
        final View itemView = holder.itemView;
        final GridSLM.LayoutParams layoutParams = GridSLM.LayoutParams.from(itemView.getLayoutParams());

        if (session.isHeader()) {
            TextView textView = (TextView) holder.mView.findViewById(R.id.header_text);
            textView.setText(session.getHeaderText());
        } else {
            holder.mSessionTitle.setText(session.getTitle());
            holder.mSpeakerName.setText(session.getSpeaker());
            holder.mStartTime.setText(DateTimeUtils.displayableTime(session.getStart()));
            holder.mDuration.setText(DateTimeUtils.getDuration(session.getStart(), session.getEnd()));
            holder.mSession = session;
        }

        layoutParams.setSlm(LinearSLM.ID);
        layoutParams.setFirstPosition(session.getSectionFirstPosition());
        itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mSessions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mSessions.get(position).isHeader() ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }
}
