package com.hasgeek.zalebi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.adapter.SessionListAdapter;
import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.network.SessionFetcher;

import java.util.ArrayList;

public class SessionFragment extends Fragment implements SessionFetcher.SessionFetchListener{

    RecyclerView mRecyclerView;

    public SessionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SessionFetcher(getActivity().getApplicationContext(), this).fetch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.session_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
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
    public void onSessionFetchSuccess(ArrayList<Session> sessions) {
        SessionListAdapter sessionListAdapter = new SessionListAdapter(sessions);
        mRecyclerView.setAdapter(sessionListAdapter);
    }

    @Override
    public void onSessionFetchFailure() {

    }
}
