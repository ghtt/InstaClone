package com.akrasnoyarov.instaclone.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    private String id;
    private String username;

    @SerializedName("account_type")
    @Expose
    private String type;

    @SerializedName("media_count")
    @Expose
    private int mediaCount;

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public String getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public String getType() {
        return type;
    }


    public int getMediaCount() {
        return mediaCount;
    }

}
