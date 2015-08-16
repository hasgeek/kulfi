package com.hasgeek.zalebi.model;

import com.orm.SugarRecord;

/**
 * Created by heisenberg on 16/08/15.
 */
public class ContactQueue extends SugarRecord<ContactQueue>{
    private String userId;
    private String userPuk;
    private String userKey;
    private String spaceId;
    private String participantUrl;

    public ContactQueue(String userPuk, String userKey, String userId, String spaceId, String participantUrl) {
        this.userPuk = userPuk;
        this.userKey = userKey;
        this.userId = userId;
        this.spaceId = spaceId;
        this.participantUrl = participantUrl;
    }

    public ContactQueue() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPuk() {
        return userPuk;
    }

    public void setUserPuk(String userPuk) {
        this.userPuk = userPuk;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getParticipantUrl() {
        return participantUrl;
    }

    public void setParticipantUrl(String participantUrl) {
        this.participantUrl = participantUrl;
    }
}
