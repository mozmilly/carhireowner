package com.example.carhireowner.hiring.interfaces;

import com.example.carhireowner.hiring.models.BookedCar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HiringInterface {

    @FormUrlEncoded
    @POST("hiring/get_booked_cars/")
    Call<List<BookedCar>> get_booked_cars(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("hiring/cancel_booked_car/")
    Call<Void> cancel_a_booked_car(
            @Field("booked_car_id") int booked_car_id
    );

    @FormUrlEncoded
    @POST("hiring/confirm_picked/")
    Call<Void> confirm_picked(
            @Field("booked_car_id") int booked_car_id
    );

    @FormUrlEncoded
    @POST("hiring/confirm_returned/")
    Call<Void> confirm_returned(
            @Field("booked_car_id") int booked_car_id
    );



}
