package com.hasgeek.zalebi.adapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.network.SessionFetcher;

import java.util.ArrayList;

public class SessionsListSyncAdapter extends AbstractThreadedSyncAdapter {
    public SessionsListSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.d("hasgeek","Sync adapter started");
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        Log.d("hasgeek","Sync adapter onPerformSync");
        new SessionFetcher(getContext(), new SessionFetcher.SessionFetchListener() {
            @Override
            public void onSessionFetchSuccess(ArrayList<Session> sessions) {

            }

            @Override
            public void onSessionFetchFailure() {

            }
        }).fetch();
    }
}
