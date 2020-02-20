package com.example.carhireowner.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carhireowner.MainActivity;
import com.example.carhireowner.R;
import com.example.carhireowner.landing;
import com.example.carhireowner.login.interfaces.LoginInterface;
import com.example.carhireowner.login.models.Login;
import com.example.carhireowner.login.otp.OtpActivity;
import com.example.carhireowner.login.otp.PhoneNumberActivity;
import com.example.carhireowner.utils.ApiUtils;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    LoginInterface loginInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView forgot = findViewById(R.id.forgot);
        forgot.setVisibility(View.GONE);

        EditText username = findViewById(R.id.username_login);
        EditText password = findViewById(R.id.password_login);
        password.setVisibility(View.GONE);

        Button login = findViewById(R.id.login);

        TextView sign_up = findViewById(R.id.sign_up);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PhoneNumberActivity.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginInterface = ApiUtils.getLoginService();

                String username1 = username.getText().toString();
                String password1 = password.getText().toString();

                loginPost(username1, password1);

            }
        });


    }
    public void loginPost(String username, String password){
        loginInterface.perform_login(username, password).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                if (response.code()==200){


//                    Toast.makeText(LoginActivity.this, login.getUsername(), Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(LoginActivity.this, OtpActivity.class);
                        intent1.putExtra("pNumber", username);
                        intent1.putExtra("intent", "login");
                        int phone = Integer.parseInt(username);
                        intent1.putExtra("phonenumber", "+254"+phone);
                        startActivity(intent1);

                } else {
                    Toast.makeText(LoginActivity.this, "Check your inputs", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Check internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(LoginActivity.this, landing.class);
        startActivity(intent);
    }
}