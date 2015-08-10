package com.hasgeek.zalebi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by heisenberg on 09/07/15.
 */

public class Attendee extends SugarRecord<Attendee> {

    @Expose
    private String company;
    @Expose
    private String fullname;

    @SerializedName("_id")
    @Expose
    private String userId;

    @SerializedName("puk")
    @Expose
    private String puk;

    @SerializedName("job_title")
    @Expose
    private String jobTitle;

    @Expose
    private String key;

    @SerializedName("space_id")
    @Expose
    private String spaceId;

    public Attendee() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof Attendee)){
            return false;
        }
        if(object == this){
            return true;
        }
        Attendee otherAttendee = (Attendee) object;
        return (this.getCompany().equals(otherAttendee.getCompany())
                && this.getFullname().equals(otherAttendee.getFullname())
                && this.getJobTitle().equals(otherAttendee.getJobTitle())
                && this.getSpaceId().equals(otherAttendee.getSpaceId()));
    }

    public void updateProperties(Attendee otherAttendee) {
        this.setCompany(otherAttendee.getCompany());
        this.setFullname(otherAttendee.getFullname());
        this.setJobTitle(otherAttendee.getJobTitle());
        this.setKey(otherAttendee.getKey());
        this.setSpaceId(otherAttendee.getSpaceId());
    }
}
