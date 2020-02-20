package com.example.carhireowner.login.interfaces;



import com.example.carhireowner.login.models.SignUp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SignUpInterface {
    @FormUrlEncoded
    @POST("sign_up_owner/")
    Call<Void> sign_up(
            @Field("username") String username,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email

    );
}
