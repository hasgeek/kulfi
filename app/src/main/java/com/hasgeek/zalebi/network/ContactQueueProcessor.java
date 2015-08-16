package com.hasgeek.zalebi.network;

import android.content.Context;
import android.util.Log;

import com.hasgeek.zalebi.model.Contact;
import com.hasgeek.zalebi.model.ContactQueue;

import java.util.List;

/**
 * Created by heisenberg on 16/08/15.
 */
public class ContactQueueProcessor implements ContactFetcher.ContactFetchListener {
    Context mContext;
    ContactFetcher mContactFetcher;

    public ContactQueueProcessor(Context context) {
        this.mContext = context;
        mContactFetcher = new ContactFetcher(mContext, this);
    }

    public void process() {
        List<ContactQueue> contactQueues = ContactQueue.listAll(ContactQueue.class);
        for (ContactQueue contactQueue : contactQueues) {
            mContactFetcher.fetch(contactQueue.getParticipantUrl());
        }
    }

    @Override
    public void onContactFetchSuccess(Contact contact) {
        List<ContactQueue> contactQueues = ContactQueue.find(ContactQueue.class, "user_id = ?", contact.getUserId());
        for (ContactQueue contactQueue : contactQueues) {
            contactQueue.delete();
        }
    }

    @Override
    public void onContactFetchFailure() {

    }
}
