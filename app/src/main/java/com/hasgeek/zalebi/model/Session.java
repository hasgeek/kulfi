package com.hasgeek.zalebi.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by heisenberg on 29/05/15.
 */
public class Session extends SugarRecord<Session> implements Parcelable {
    private String title;
    private String description;
    private String start;
    private String end;
    private String speaker;
    private String room;

    @SerializedName("space_id")
    @Expose
    private String spaceId;

//    @SerializedName("id")
//    @Expose
    private Long sessionId;

    public Session(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    protected Session(Parcel in) {
        title = in.readString();
        description = in.readString();
        start = in.readString();
        end = in.readString();
        speaker = in.readString();
        spaceId = in.readString();
        room = in.readString();
        sessionId = in.readByte() == 0x00 ? null : in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(speaker);
        dest.writeString(spaceId);
        String roomString = (room == null) ? "" : room;
        dest.writeString(roomString);
        if (sessionId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(sessionId);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Session> CREATOR = new Parcelable.Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    public void saveOrUpdate() {
        setSessionId(getId());
        setId(null);
        List<Session> existingSessions = Session.find(Session.class, "session_id = ?", ""+sessionId);
        if (existingSessions.size() > 0){
            Log.d("hasgeek","Data exists, so updating");
            Session existingSession = existingSessions.get(0);
            setId(existingSession.getId());
        }
        save();
    }
}