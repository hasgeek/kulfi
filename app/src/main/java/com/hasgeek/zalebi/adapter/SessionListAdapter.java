package com.hasgeek.zalebi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.model.Session;

import java.util.ArrayList;

/**
 * Created by heisenberg on 29/05/15.
 */
public class SessionListAdapter extends ArrayAdapter<Session> {

    LayoutInflater mLayoutInflater;
    int mResource;
    Context mContext;
    ArrayList<Session> mSessions;

    public SessionListAdapter(Context context, int resource, ArrayList<Session> sessions) {
        super(context, resource, sessions);
        mResource = resource;
        mContext = context;
        mSessions =sessions;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mResource, parent, false);

        Session session = mSessions.get(position);

        TextView sessionTitle = (TextView) convertView.findViewById(R.id.session_title);
        TextView speakerName = (TextView) convertView.findViewById(R.id.speaker_name);

        sessionTitle.setText(session.getTitle());
        speakerName.setText(session.getSpeaker());
        return convertView;
    }
}
