package com.example.carhireowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carhireowner.login.LoginActivity;
import com.example.carhireowner.login.otp.PhoneNumberActivity;


public class landing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);


        Button login = findViewById(R.id.Button02);
        Button signUp = findViewById(R.id.Button03);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(landing.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(landing.this, PhoneNumberActivity.class);
                startActivity(intent);
            }
        });
    }
}