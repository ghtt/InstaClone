package com.akrasnoyarov.instaclone.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.akrasnoyarov.instaclone.ApplicationPreferences;
import com.akrasnoyarov.instaclone.database.InstaCloneDatabase;
import com.akrasnoyarov.instaclone.database.entity.MediaEntity;

import java.util.List;

public class InstaCloneRepository {
    private static final String LOG_TAG = InstaCloneRepository.class.getSimpleName();
    private final InstaCloneDatabase mDb;
    private LiveData<List<MediaEntity>> mAllUserMedia;

    public InstaCloneRepository(Application application) {
        mDb = InstaCloneDatabase.getInstance(application.getApplicationContext());
        ApplicationPreferences preferences = new ApplicationPreferences(application);
        String username = preferences.getString(ApplicationPreferences.USER_NAME);
        mAllUserMedia = mDb.getAllUserMedia(username);
    }


    private static class AddMediaAsyncTask extends AsyncTask<MediaEntity, Void, Void> {
        private InstaCloneDatabase mDb;

        public AddMediaAsyncTask(InstaCloneDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(MediaEntity... mediaEntities) {
            mDb.addMedia(mediaEntities[0]);
            return null;
        }
    }

    public LiveData<List<MediaEntity>> getAllUserMedia() {
        return mAllUserMedia;
    }

    public void addMedia(MediaEntity mediaEntity) {
        new AddMediaAsyncTask(mDb).execute(mediaEntity);
    }

    public void deleteMedia(MediaEntity mediaEntity) {
        mDb.deleteMedia(mediaEntity);
    }

    public MediaEntity getMediaById(String mediaId) {
        return mDb.getMediaById(mediaId);
    }

}
