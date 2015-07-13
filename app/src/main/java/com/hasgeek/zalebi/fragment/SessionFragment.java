package com.hasgeek.zalebi.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.hasgeek.zalebi.sync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "hasgeek.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";

    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;
    private Account mAccount;

    public SessionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new SessionFetcher(getActivity().getApplicationContext(), this).fetch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_session, container, false);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new SessionListAdapter());
        mAccount = createSyncAccount(mRecyclerView.getContext());
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY,settingsBundle );
        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                SYNC_INTERVAL);
        Log.d("hasgeek","initiated periodic sync for SyncAdapter");
        return mRecyclerView;
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

    /** Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        Context.ACCOUNT_SERVICE);
        return newAccount;
    }

}
