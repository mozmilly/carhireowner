package com.example.carhireowner.utils;


import com.example.carhireowner.car.interfaces.CarInterface;
import com.example.carhireowner.firebase_push.interfaces.TokenInterface;
import com.example.carhireowner.hiring.interfaces.HiringInterface;
import com.example.carhireowner.login.interfaces.LoginInterface;
import com.example.carhireowner.login.interfaces.SignUpInterface;

public class ApiUtils {
    private ApiUtils() {}
    public static final String BASE_URL = "http://carhiremodule.pythonanywhere.com/";

    public static LoginInterface getLoginService(){
        return RetrofitClient.getClient(BASE_URL).create(LoginInterface.class);
    }

    public static SignUpInterface getSignUpService(){
        return RetrofitClient.getClient(BASE_URL).create(SignUpInterface.class);
    }

    public static CarInterface getCarService(){
        return RetrofitClient.getClient(BASE_URL).create(CarInterface.class);
    }

    public static HiringInterface getHiringService(){
        return RetrofitClient.getClient(BASE_URL).create(HiringInterface.class);
    }

    public static TokenInterface getTokenService(){
        return RetrofitClient.getClient(BASE_URL).create(TokenInterface.class);
    }

}
