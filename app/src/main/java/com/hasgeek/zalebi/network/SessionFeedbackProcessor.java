package com.hasgeek.zalebi.network;

import android.content.Context;
import android.util.Log;

import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.model.SessionFeedback;

import java.util.List;

/**
 * Created by karthikbalakrishnan on 08/09/15.
 */
public class SessionFeedbackProcessor implements SessionFeedbackSubmitter.SessionFeedbackSubmitListener {

    Context mContext;
    SessionFeedbackSubmitter mSessionFeedbackSubmitter;

    public SessionFeedbackProcessor(Context context) {
        this.mContext = context;
        mSessionFeedbackSubmitter = new SessionFeedbackSubmitter(mContext, this);
    }

    public void process() {
        List<SessionFeedback> sessionFeedbackList = SessionFeedback.find(SessionFeedback.class,"sync_status = ?", "false");
        for(SessionFeedback sessionFeedback : sessionFeedbackList) {
            Log.d("hasgeek", "Syncing session feedback for space " + sessionFeedback.getSpaceId());
            mSessionFeedbackSubmitter.submit(sessionFeedback);
        }
    }

    @Override
    public void onSessionFeedbackSubmitFailure() {

    }

    @Override
    public void onSessionFeedbackSubmitSuccess(SessionFeedback sessionFeedback) {
        sessionFeedback.setSyncStatus(true);
        sessionFeedback.save();
        Log.d("hasgeek", "Successfully synced session feedback for session id " + sessionFeedback.getSessionId());
    }
}
