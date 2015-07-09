package com.hasgeek.zalebi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hasgeek.zalebi.adapter.SessionsListSyncAdapter;

/**
 * Created by leena on 08/07/15.
 */
public class SessionSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static SessionsListSyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (syncAdapter == null)
                syncAdapter = new SessionsListSyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
