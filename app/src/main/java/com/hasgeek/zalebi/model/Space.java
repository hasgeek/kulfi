package com.hasgeek.zalebi.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.util.List;

public class Space extends SugarRecord<Space> {

    private String title;
    private String url;
    @SerializedName("json_url")
    private String jsonURL;
    private String start;
    private String end;
    private String website;
    private Long spaceId;

    public Space(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJsonURL() {
        return jsonURL;
    }

    public void setJsonURL(String jsonURL) {
        this.jsonURL = jsonURL;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }
    public void saveOrUpdate() {
        setSpaceId(getId());
        setId(null);
        List<Space> existingSpaces = Space.find(Space.class, "space_id = ?", ""+spaceId);
        if (existingSpaces.size() > 0){
            Space existingSpace = existingSpaces.get(0);
            setId(existingSpace.getId());
        }
        save();
    }
}
