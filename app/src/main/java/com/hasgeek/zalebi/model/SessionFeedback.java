package com.hasgeek.zalebi.model;

import com.orm.SugarRecord;

/**
 * Created by karthikbalakrishnan on 27/08/15.
 */
public class SessionFeedback extends SugarRecord<SessionFeedback> {

    String spaceId;
    Long sessionId;
    String text;
    int rating;
    boolean syncStatus;

    public SessionFeedback() {
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(boolean syncStatus) {
        this.syncStatus = syncStatus;
    }
}
