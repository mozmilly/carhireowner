package com.example.carhireowner.hiring;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carhireowner.MainActivity;
import com.example.carhireowner.R;
import com.example.carhireowner.hiring.interfaces.HiringInterface;
import com.example.carhireowner.hiring.models.BookedCar;
import com.example.carhireowner.ui.notifications.NotificationsFragment;
import com.example.carhireowner.utils.ApiUtils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class BookedCarDetails extends AppCompatActivity {
    TextView make, number_plate, color, seater, price, user_phone, status, user_name, no_of_days;
    ImageView car_image, owner_image;
    BookedCar bookedCar;
    Button cancel, confirmPicked, confirmReturned;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_car_details);


        number_plate = findViewById(R.id.car_detail_number_plate);
        color = findViewById(R.id.car_detail_color);
        seater = findViewById(R.id.car_detail_seater);
        price = findViewById(R.id.car_detail_price_per_day);
        user_phone = findViewById(R.id.user_phone_number);
        car_image = findViewById(R.id.car_detail_image);
        status = findViewById(R.id.car_detail_status);
        user_name = findViewById(R.id.user_name);
        bookedCar = NotificationsFragment.getBookedCar();
        cancel = findViewById(R.id.cancel);
        confirmPicked = findViewById(R.id.confirm_picked);
        confirmReturned = findViewById(R.id.confirm_returned);
        no_of_days = findViewById(R.id.no_of_days);


        number_plate.setText(("Number Plate: "+bookedCar.getCar().getNumber_plate() ));
        color.setText(("Color: "+bookedCar.getCar().getColor() ));
        seater.setText(("Seater: "+bookedCar.getCar().getSeaters()));
        price.setText(("Price total: "+(bookedCar.getCar().getPrice_per_day()*bookedCar.getNo_of_days())));
        user_phone.setText(("Client Phone: "+bookedCar.getUser().getUsername() ));
        status.setText(("Status: "+bookedCar.getStatus() ));
        user_name.setText(("Client Name: "+bookedCar.getUser().getFirst_name()+" "+bookedCar.getUser().getLast_name() ));
        no_of_days.setText(("No of Days: "+bookedCar.getNo_of_days()));
        if (bookedCar.getCar().getPhoto()!=null){
            if (URLUtil.isValidUrl("https://carhiremodule.pythonanywhere.com"+bookedCar.getCar().getPhoto())){
                Picasso.with(BookedCarDetails.this)
                        .load("https://carhiremodule.pythonanywhere.com"+bookedCar.getCar().getPhoto())
                        .placeholder(R.drawable.cab)
                        .into(car_image);
            }
        }
        if (bookedCar.getStatus().equalsIgnoreCase("Booked")){
            confirmReturned.setVisibility(GONE);
        } else if (bookedCar.getStatus().equalsIgnoreCase("Canceled")){
            cancel.setVisibility(GONE);
            confirmReturned.setVisibility(GONE);
            confirmPicked.setVisibility(GONE);
        } else if (bookedCar.getStatus().equalsIgnoreCase("Picked")){
            cancel.setVisibility(GONE);
            confirmPicked.setVisibility(GONE);
        } else {
            cancel.setVisibility(GONE);
            confirmReturned.setVisibility(GONE);
            confirmPicked.setVisibility(GONE);
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HiringInterface hiringInterface = ApiUtils.getHiringService();
                hiringInterface.cancel_a_booked_car(bookedCar.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code()==200){
                            Intent intent = new Intent(BookedCarDetails.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        confirmPicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HiringInterface hiringInterface = ApiUtils.getHiringService();
                hiringInterface.confirm_picked(bookedCar.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code()==200){
                            Intent intent = new Intent(BookedCarDetails.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(BookedCarDetails.this, "Car has not been returned", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        confirmReturned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HiringInterface hiringInterface = ApiUtils.getHiringService();
                hiringInterface.confirm_returned(bookedCar.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code()==200){
                            Intent intent = new Intent(BookedCarDetails.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });




    }
}
