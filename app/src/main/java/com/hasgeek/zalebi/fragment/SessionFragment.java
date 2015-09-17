package com.hasgeek.zalebi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.adapter.SessionListAdapter;
import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.reader.SessionsReader;
import com.tonicartos.superslim.LayoutManager;
import java.util.List;

public class SessionFragment extends Fragment implements SessionsReader.SessionReadListener{

    RecyclerView mRecyclerView;
    private int lastFirstVisiblePosition = 0;

    public SessionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_session, container, false);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LayoutManager(mRecyclerView.getContext()));
        return mRecyclerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new SessionsReader(getActivity().getApplicationContext(),this).readSessions();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSessionReadSuccess(List<Session> sessions) {
        SessionListAdapter sessionListAdapter = new SessionListAdapter(sessions);
        mRecyclerView.setAdapter(sessionListAdapter);
    }

    @Override
    public void onSessionReadFailure() {

    }

    @Override
    public void onPause() {
        super.onPause();
        lastFirstVisiblePosition = ((LayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.getLayoutManager().scrollToPosition(lastFirstVisiblePosition);
    }


}
