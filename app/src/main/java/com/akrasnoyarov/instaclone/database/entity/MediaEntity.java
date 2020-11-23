package com.akrasnoyarov.instaclone.database.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_media")
public class MediaEntity {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    @ColumnInfo(name = "caption")
    private String caption;

    @ColumnInfo(name = "permalink")
    @NonNull
    private String permalink;

    @ColumnInfo(name = "thumbnail_url")
    private String thumbnailUrl;

    @ColumnInfo(name = "username")
    @NonNull
    private String username;

    @ColumnInfo(name = "media_type")
    private String mediaType;

    @ColumnInfo(name = "media_url")
    private String mediaUrl;

    public MediaEntity(@NonNull String id, String caption, @NonNull String permalink, String thumbnailUrl, @NonNull String username, String mediaType, String mediaUrl) {
        this.id = id;
        this.caption = caption;
        this.permalink = permalink;
        this.thumbnailUrl = thumbnailUrl;
        this.username = username;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @NonNull
    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(@NonNull String permalink) {
        this.permalink = permalink;
    }


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}