package com.akrasnoyarov.instaclone;

import android.app.Application;

import com.akrasnoyarov.instaclone.api.AppExecutors;
import com.akrasnoyarov.instaclone.database.InstaCloneDatabase;
import com.akrasnoyarov.instaclone.repository.InstaCloneRepository;

public class InstaCloneApp extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public InstaCloneDatabase getDatabase() {
        return InstaCloneDatabase.getInstance(this);
    }

    public InstaCloneRepository getRepository() {
        return new InstaCloneRepository(this);
    }
}
