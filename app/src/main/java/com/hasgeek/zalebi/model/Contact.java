package com.hasgeek.zalebi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by heisenberg on 24/07/15.
 */
public class Contact extends SugarRecord<Contact> {
    @Expose
    private String company;

    @Expose
    private String email;

    @Expose
    private String fullname;

    @SerializedName("job_title")
    @Expose
    private String jobTitle;

    @SerializedName("_id")
    @Expose
    private String userId;

    @SerializedName("puk")
    @Expose
    private String puk;

    @Expose
    private String phone;

    @Expose
    private String twitter;

    @SerializedName("space_id")
    @Expose
    private String spaceId;


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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }
}
