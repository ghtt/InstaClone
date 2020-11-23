package com.akrasnoyarov.instaclone.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InstagramGraphService {
    @GET("me?")
    Call<User> getUser(@Query("fields") String fields, @Query("access_token") String accessToken);

    @GET("me/media")
    Call<Data> getUserMedia(@Query("fields") String fields, @Query("access_token") String accessToken);

}
