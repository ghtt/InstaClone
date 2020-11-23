package com.akrasnoyarov.instaclone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.akrasnoyarov.instaclone.database.entity.MediaEntity;
import com.akrasnoyarov.instaclone.repository.InstaCloneRepository;

import java.util.List;

public class PostFeedViewModel extends AndroidViewModel {
    private InstaCloneRepository mRepository;
    private LiveData<List<MediaEntity>> mAllUserMedia;

    public PostFeedViewModel(@NonNull Application application) {
        super(application);
        mRepository = new InstaCloneRepository(application);
        mAllUserMedia = mRepository.getAllUserMedia();
    }

    public void addMedia(MediaEntity mediaEntity) {
        mRepository.addMedia(mediaEntity);
    }

    public LiveData<List<MediaEntity>> getAllUserMedia() {
        return mAllUserMedia;
    }
}
