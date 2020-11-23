package com.akrasnoyarov.instaclone.api;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface InstagramOAuthService {

    @FormUrlEncoded
    @POST("access_token")
    Call<ResponseBody> getUserTokenUsingFields(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String authCode,
            @Field("grant_type") String grantType,
            @Field("redirect_uri") String redirectUri);

}
