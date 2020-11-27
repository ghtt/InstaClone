package com.akrasnoyarov.instaclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.akrasnoyarov.instaclone.adapter.PostFeedAdapter;
import com.akrasnoyarov.instaclone.api.ClientController;
import com.akrasnoyarov.instaclone.database.entity.MediaEntity;
import com.akrasnoyarov.instaclone.repository.InstaCloneRepository;
import com.akrasnoyarov.instaclone.viewmodel.PostFeedViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity implements AuthListener {
    public static String sAuthToken;
    public static String sUserToken;
    private RecyclerView mRecyclerViewPostFeed;
    private PostFeedAdapter mAdapter;
    private InstaCloneRepository mRepository;
    private PostFeedViewModel mPostFeedViewModel;
    private ClientController mClient;
    private Button mButtonSignIn;
    private ApplicationPreferences mPreferences;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializePreferences();
        Log.d("myLogs", "activity = " + this.toString());

        mLoading = findViewById(R.id.pb_loading);
        mButtonSignIn = findViewById(R.id.button_login);

        mRecyclerViewPostFeed = findViewById(R.id.rv_post_feed);
        mAdapter = new PostFeedAdapter(this);
        mRecyclerViewPostFeed.setAdapter(mAdapter);
        mRecyclerViewPostFeed.setLayoutManager(new LinearLayoutManager(this));

        Log.d("myLogs", "MainActivity onCreate");

        if (!mPreferences.getString(ApplicationPreferences.USER_NAME).equals("")) {
            mButtonSignIn.setVisibility(View.GONE);
            mRecyclerViewPostFeed.setVisibility(View.VISIBLE);

            mRepository = new InstaCloneRepository(getApplication());

            mPostFeedViewModel = new ViewModelProvider(this).get(PostFeedViewModel.class);
            mPostFeedViewModel.getAllUserMedia().observe(this, new Observer<List<MediaEntity>>() {
                @Override
                public void onChanged(final List<MediaEntity> mediaEntities) {
                    mAdapter.setMedia(mediaEntities);
                }
            });
        }
    }


    @Override
    protected void onStart() {
        Log.d("myLogs", "onStart");
        super.onStart();
        mClient = ClientController.getInstance(this);
        if (!mPreferences.getString(ApplicationPreferences.USER_TOKEN).equals("")) {
            sUserToken = mPreferences.getString(ApplicationPreferences.USER_TOKEN);
            mClient.getUserMedia(mPostFeedViewModel);
        } else {
            mButtonSignIn.setVisibility(View.VISIBLE);
            mRecyclerViewPostFeed.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pref_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent startSettingsIntent = new Intent(this, SettingsActivity.class);
                startSettingsIntent.putExtra(ApplicationPreferences.USER_NAME, mPreferences.getString(ApplicationPreferences.USER_NAME));
                startActivity(startSettingsIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOAuthTokenReceived(String oAuthToken) {
        Log.d("myLogs", "onOAuthTokenReceived: " + oAuthToken);
        mPreferences.putString(ApplicationPreferences.OAUTH_TOKEN, oAuthToken);
        sAuthToken = oAuthToken;
        mLoading.setVisibility(View.VISIBLE);
        mClient.getUserToken(this);
    }

    @Override
    public void onUserTokenReceived(String token) {
        // save user token in memory and in shared preferences
        sUserToken = token;
        mPreferences.putString(ApplicationPreferences.USER_TOKEN, token);
        Log.d("myLogs", "onUserTokenReceived: " + sUserToken);

        // if username is empty, get it from api, else load the media
        if (mPreferences.getString(ApplicationPreferences.USER_NAME).equals("")) {
            mClient.getUser();
        } else {
            mButtonSignIn.setVisibility(View.GONE);
            mRecyclerViewPostFeed.setVisibility(View.VISIBLE);
            onUsernameReceived(mPreferences.getString(ApplicationPreferences.USER_NAME));
        }

    }

    @Override
    public void onUsernameReceived(String username) {
        mRecyclerViewPostFeed.setVisibility(View.VISIBLE);
        mButtonSignIn.setVisibility(View.GONE);

        Log.d("myLogs", "onUsernameReceived");
        // save username in pref
        mPreferences.putString(ApplicationPreferences.USER_NAME, username);

        // init room db + viewmodel
        mRepository = new InstaCloneRepository(getApplication());
        mPostFeedViewModel = new ViewModelProvider(this).get(PostFeedViewModel.class);
        mPostFeedViewModel.getAllUserMedia().observe(this, new Observer<List<MediaEntity>>() {
            @Override
            public void onChanged(final List<MediaEntity> mediaEntities) {
                mAdapter.setMedia(mediaEntities);
            }
        });

        // get all user media
        mClient.getUserMedia(mPostFeedViewModel);
        mLoading.setVisibility(View.GONE);

    }

    @Override
    public void onReauthRequired() {
        mClient.authenticate(this);
    }

    private void initializePreferences() {
        mPreferences = new ApplicationPreferences(this);
    }

    public void signIn(View v) {
        mClient.authenticate(this);
    }

}