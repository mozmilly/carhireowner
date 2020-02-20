package com.example.carhireowner.login.interfaces;


import com.example.carhireowner.login.models.Login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {
    @FormUrlEncoded
    @POST("login_owner/")
    Call<Void> perform_login(
            @Field("username") String username,
            @Field("password") String password
    );
}
