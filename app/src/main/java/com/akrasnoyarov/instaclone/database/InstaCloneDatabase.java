package com.akrasnoyarov.instaclone.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.akrasnoyarov.instaclone.api.AppExecutors;
import com.akrasnoyarov.instaclone.database.dao.MediaDao;
import com.akrasnoyarov.instaclone.database.entity.MediaEntity;

import java.util.List;

@Database(entities = {MediaEntity.class}, version = 1)
public abstract class InstaCloneDatabase extends RoomDatabase {
    public abstract MediaDao mediaDao();

    private static final String DATABASE_NAME = "insta-clone-db";
    private static final Object LOCK = new Object();

    private static InstaCloneDatabase sInstance;

    public static InstaCloneDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    Log.d("myLogs", "Creating new database instance");
                    sInstance = Room.databaseBuilder(context, InstaCloneDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
//                            .setQueryExecutor(executors.diskIO())
                            .build();
                }
            }
        }
        Log.d("myLogs", "Getting the database instance");
        return sInstance;
    }

    public LiveData<List<MediaEntity>> getAllUserMedia(String username) {
        return mediaDao().getAllUserMedia(username);
    }

    public void addMedia(MediaEntity mediaEntity) {
        mediaDao().addMedia(mediaEntity);
    }

    public void deleteMedia(MediaEntity mediaEntity) {
        mediaDao().deleteMedia(mediaEntity);
    }

    public MediaEntity getMediaById(String mediaId) {
        return mediaDao().getMediaById(mediaId);
    }

}
