package com.hasgeek.zalebi.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.adapter.SessionListAdapter;
import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.network.SessionFetcher;

import java.util.ArrayList;

public class SessionFragment extends Fragment implements SessionFetcher.SessionFetchListener{

    RequestQueue requestQueue;

    public SessionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SessionFetcher(getActivity().getApplicationContext(), this).fetch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false);
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
        ListView listView = (ListView) getActivity().findViewById(R.id.session_list);
        SessionListAdapter sessionListAdapter = new SessionListAdapter(getActivity(), R.layout.session_list_item, sessions);
        listView.setAdapter(sessionListAdapter);
    }

    @Override
    public void onSessionFetchFailure() {

    }
}
