package com.example.carhireowner.firebase_push.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TokenInterface {

    @FormUrlEncoded
    @POST("token/upload_token/")
    Call<Void> upload_token(
            @Field("username") String username,
            @Field("token") String token
    );
}
