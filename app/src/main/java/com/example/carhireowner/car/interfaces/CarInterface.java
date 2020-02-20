package com.example.carhireowner.car.interfaces;

import com.example.carhireowner.car.models.Car;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CarInterface {

    @FormUrlEncoded
    @POST("car/get_my_cars/")
    Call<List<Car>> get_my_cars(
            @Field("username") String username,
            @Field("page") int page
    );


    @Multipart
    @POST("car/upload_car/")
    Call<Void> upload_car(
            @Part("username") RequestBody username,
            @Part("make") RequestBody make,
            @Part("number_plate") RequestBody number_plate,
            @Part("color") RequestBody color,
            @Part("seaters") RequestBody seaters,
            @Part("price_per_day") RequestBody price_per_day,
            @Part("location") RequestBody location,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name
    );


    @Multipart
    @POST("car/edit_car/")
    Call<Void> edit_car(
            @Part("car_id") RequestBody car_id,
            @Part("color") RequestBody color,
            @Part("price_per_day") RequestBody price_per_day,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name
    );
}
