package com.hasgeek.zalebi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hasgeek.zalebi.sync.adapter.SessionSyncAdapter;

/**
 * Created by leena on 08/07/15.
 */
public class SessionSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static SessionSyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("hasgeek","inside SessionSync Service");
        synchronized (sSyncAdapterLock) {
            if (syncAdapter == null)
                syncAdapter = new SessionSyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
