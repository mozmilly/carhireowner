package com.example.carhireowner.login.interfaces;



import com.example.carhireowner.login.models.SignUp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SignUpInterface {
    @Multipart
    @POST("sign_up_owner/")
    Call<Void> sign_up(
            @Part("username") RequestBody username,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part file

    );
}
