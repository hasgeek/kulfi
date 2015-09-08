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

    public SessionFeedback() {
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
}
