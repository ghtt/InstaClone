package com.akrasnoyarov.instaclone.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.akrasnoyarov.instaclone.database.entity.MediaEntity;

import java.util.List;

@Dao
public interface MediaDao {

    @Query("SELECT * FROM user_media WHERE username = :username")

    LiveData<List<MediaEntity>> getAllUserMedia(String username);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addMedia(MediaEntity mediaEntity);

    @Delete
    void deleteMedia(MediaEntity mediaEntity);

    @Query("SELECT * FROM user_media WHERE id = :mediaId")
    MediaEntity getMediaById(String mediaId);
}
