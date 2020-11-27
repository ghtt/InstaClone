package com.akrasnoyarov.instaclone.api;


import android.content.Context;
import android.util.Log;

import com.akrasnoyarov.instaclone.AuthListener;
import com.akrasnoyarov.instaclone.AuthenticationDialog;
import com.akrasnoyarov.instaclone.MainActivity;
import com.akrasnoyarov.instaclone.R;
import com.akrasnoyarov.instaclone.database.entity.MediaEntity;
import com.akrasnoyarov.instaclone.viewmodel.PostFeedViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientController {
    private static final String BASE_URL = "https://graph.instagram.com/";
    private static ClientController sInstance;
    private static boolean sAuthorized = false;

    private AuthListener mListener;
    private InstagramGraphService service;

    private ClientController(AuthListener listener) {
        mListener = listener;

        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        service = retrofit.create(InstagramGraphService.class);

    }

    public static ClientController getInstance(AuthListener listener) {
        if (sInstance == null) {
            synchronized (ClientController.class) {
                if (sInstance == null) {
                    sInstance = new ClientController(listener);
                }
            }
        }
        return sInstance;
    }

    public void authenticate(Context context) {
        Log.d("myLogs", "authenticate");
        AuthenticationDialog dialog = new AuthenticationDialog(context, mListener);
        dialog.show();
    }

    public void getUserToken(Context context) {
        sAuthorized = false;

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Retrofit retrofitOauth = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.auth_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // create service
        InstagramOAuthService oAuthService = retrofitOauth.create(InstagramOAuthService.class);

        // generate POST request
        Call<ResponseBody> call = oAuthService.getUserTokenUsingFields(
                context.getResources().getString(R.string.instagram_app_id),
                context.getResources().getString(R.string.client_secret),
                MainActivity.sAuthToken,
                context.getResources().getString(R.string.grant_type),
                context.getResources().getString(R.string.redirect_uri));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("myLogs", "request" + call.request().toString());
                    Log.d("myLogs", "response" + response.toString());
                    if (response.body() != null) {
                        String userToken = response.body().getAccessToken();
                        mListener.onUserTokenReceived(userToken);
                    }
                } else {
                    Log.d("myLogs", "invalid request");
                    Log.d("myLogs", call.request().toString() + "\n" + call.request().body());
                    Log.d("myLogs", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("myLogs", "FAIL");
            }
        });
    }


    public void getUser() {
        Log.d("myLogs", "enter getUser");
        Log.d("myLogs", "getUser Authorized = " + sAuthorized);
        if (!sAuthorized) {
            Call<User> call = service.getUser("account_type,id,username,media_count", MainActivity.sUserToken);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Log.d("myLogs", "getUser -> onResponse");
                        User user = response.body();

                        Log.d("myLogs", user.getUsername());
//                        Log.d("myLogs", user.getId());

                        String username = user.getUsername();

                        sAuthorized = true;
                        mListener.onUsernameReceived(username);

                    } else {
                        try {
                            Log.d("myLogs", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("myLogs", "getUser onFailure. " + t.getLocalizedMessage());
                }
            });
        }
    }

    public void getUserMedia(PostFeedViewModel viewModel) {
        Log.d("myLogs", "enter getUserMedia");
        Log.d("myLogs", "getUserMedia Authorized = " + sAuthorized);

        if (sAuthorized) {
            Call<Data> call = service.getUserMedia("caption,id,permalink,thumbnail_url,username,media_type,media_url", MainActivity.sUserToken);
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    if (response.isSuccessful()) {
                        Data mediaData = response.body();

                        for (UserMedia media : mediaData.getData()) {
                            MediaEntity mediaEntity = new MediaEntity(
                                    media.getId(),
                                    media.getCaption(),
                                    media.getPermalink(),
                                    media.getThumbnailUrl(),
                                    media.getUsername(),
                                    media.getMediaType(),
                                    media.getMediaUrl()
                            );

                            viewModel.addMedia(mediaEntity);
//                            Log.d("myLogs", media.getMediaType() + " url: " + media.getMediaUrl() + "\ncaption=" + media.getCaption());
                        }

                        Log.d("myLogs", "userMedia are received");
                    } else {
                        try {
                            okhttp3.ResponseBody errorBody = response.errorBody();
                            if (errorBody != null) {
                                String error = errorBody.string();
                                if (error.contains("Error validating access token: Session has expired")) {
                                    Log.d("myLogs", "userToken is expired, trying to renew");
                                    mListener.onReauthRequired();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Log.d("myLogs", "getUser onFailure. " + t.getLocalizedMessage());
                }
            });
        }
    }
}
