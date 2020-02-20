package com.example.carhireowner.views;

import android.os.CountDownTimer;

import com.example.carhireowner.login.otp.OtpActivity;


/**
 * Created by Jerry on 10/31/2017.
 * This is CountDownTimer sub class, which will override it's abstract methods.
 */

public class MyCountDownTimer extends CountDownTimer {

    // This variable refer to the source activity which use this CountDownTimer object.


    private OtpActivity otpActivity;

    public void setSourceActivity(OtpActivity otpActivity) {
        this.otpActivity = otpActivity;
    }


    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
         if (this.otpActivity!=null){
            this.otpActivity.onCountDownTimerTickEvent(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (this.otpActivity!=null){
            this.otpActivity.onCountDownTimerFinishEvent();
        }
    }
}