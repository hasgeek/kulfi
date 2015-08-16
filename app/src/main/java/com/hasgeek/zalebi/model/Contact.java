package com.hasgeek.zalebi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by heisenberg on 24/07/15.
 */
public class Contact extends SugarRecord<Contact> implements Parcelable{
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

    public Contact() {
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

    protected Contact(Parcel in) {
        company = in.readString();
        fullname = in.readString();
        jobTitle = in.readString();
        email = in.readString();
        phone = in.readString();
        twitter = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(company);
        dest.writeString(fullname);
        dest.writeString(jobTitle);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(twitter);
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
