package com.hasgeek.zalebi.sync;

import android.content.Context;

import com.hasgeek.zalebi.network.AttendeeListFetcher;
import com.hasgeek.zalebi.network.ContactQueueProcessor;
import com.hasgeek.zalebi.network.SessionFetcher;
import com.hasgeek.zalebi.network.SpaceFetcher;

public class SyncProvider {

    public void sync(Context context){
        syncSpaces(context);
        syncSessions(context);
        syncAttendees(context);
        new ContactQueueProcessor(context).process();
    }

    private void syncAttendees(Context context) {
        new AttendeeListFetcher(context).syncAttendees();
    }

    private void syncSessions(Context context) {
        new SessionFetcher(context).syncSessions();
    }

    private void syncSpaces(Context context){
        new SpaceFetcher(context).fetch();
    }

}
