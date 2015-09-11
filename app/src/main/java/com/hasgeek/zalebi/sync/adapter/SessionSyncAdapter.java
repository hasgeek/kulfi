package com.hasgeek.zalebi.sync.adapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.hasgeek.zalebi.sync.SyncProvider;

public class SessionSyncAdapter extends AbstractThreadedSyncAdapter {
    public SessionSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.d("hasgeek", "Sync adapter started");
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        Log.d("hasgeek","Sync adapter onPerformSync");
        new SyncProvider().sync(getContext());
    }

}
