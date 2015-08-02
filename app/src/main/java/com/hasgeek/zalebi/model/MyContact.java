package com.hasgeek.zalebi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by heisenberg on 24/07/15.
 */
public class MyContact extends SugarRecord<MyContact> {
    @Expose
    private String company;

    @Expose
    private String email;

    @Expose
    private String fullname;

    @SerializedName("job_title")
    @Expose
    private String jobTitle;


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
}
