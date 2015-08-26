package com.hasgeek.zalebi.reader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hasgeek.zalebi.activity.TalkFunnelActivity;
import com.hasgeek.zalebi.model.Session;
import com.hasgeek.zalebi.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class SessionsReader {

    private final Context context;
    private final SessionReadListener listener;

    public SessionsReader(Context context,SessionReadListener sessionReadListener){
        this.context = context;
        this.listener = sessionReadListener;
    }

    public List<Session> readSessions(){
        List<Session> sessions = new ArrayList<>();
        try {
            sessions = Session.find(Session.class,"space_id = ?", TalkFunnelActivity.SPACE_ID);
            listener.onSessionReadSuccess(addHeadersToSessions(sessions));
        }
        catch (Exception ex){
            listener.onSessionReadFailure();
            Log.e("hasgeek",
                    "Exception while reading sessions  "+ex.getMessage());
        }
        return sessions;
    }

    public interface SessionReadListener {
        void onSessionReadSuccess(List<Session> sessions);
        void onSessionReadFailure();
    }

    private List<Session> addHeadersToSessions(List<Session> sessions){
        List<Session> newSessions = new ArrayList<>();
        String lastHeader = "";
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < sessions.size(); i++) {
            Session session = sessions.get(i);
            String header = DateTimeUtils.displayableDate(session.getStart(), true);
            if (!TextUtils.equals(lastHeader, header)) {
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;
                Session headerSession = new Session(true, sectionFirstPosition);
                headerSession.setHeaderText(("DAY " + headerCount + ", " +header).toUpperCase());
                newSessions.add(headerSession);
            }
            session.setIsHeader(false);
            session.setSectionFirstPosition(sectionFirstPosition);
            newSessions.add(session);
        }
        return newSessions;
    }
}
