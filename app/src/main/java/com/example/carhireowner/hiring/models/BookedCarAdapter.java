package com.example.carhireowner.hiring.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carhireowner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookedCarAdapter extends  RecyclerView.Adapter<BookedCarAdapter.MyViewHolder>{

    List<BookedCar> bookedCarList;
    Context context;


    public BookedCarAdapter(List<BookedCar> bookedCarList, Context context) {
        this.bookedCarList = bookedCarList;
        this.context = context;
    }

    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booked_car_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.price.setText(("Price Per Day: "+bookedCarList.get(position).getCar().getPrice_per_day()));
        holder.number_plate.setText(("Number Plate: "+bookedCarList.get(position).getCar().getNumber_plate()));
        holder.status.setText(("Status: "+bookedCarList.get(position).getStatus()));
        holder.date.setText(("Date for hire: "+bookedCarList.get(position).getDate_for_hire()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(position);
            }
        });

        if (bookedCarList.get(position).getCar().getPhoto()!=null){
            if (URLUtil.isValidUrl("https://carhiremodule.pythonanywhere.com"+bookedCarList.get(position).getCar().getPhoto())){
                Picasso.with(context)
                        .load("https://carhiremodule.pythonanywhere.com"+bookedCarList.get(position).getCar().getPhoto())
                        .placeholder(R.drawable.cab)
                        .into(holder.imageView);
            }
        }


    }

    @Override
    public int getItemCount() {
        return bookedCarList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView price, number_plate, status, date;
        ImageView imageView;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            price = view.findViewById(R.id.booked_car_price);
            number_plate = view.findViewById(R.id.booked_car_number_plate);
            status = view.findViewById(R.id.booked_car_status);
            imageView = view.findViewById(R.id.booked_car_image);
            date = view.findViewById(R.id.booked_car_date_for_hire);
            parent = view.findViewById(R.id.booked_car_parent);
        }
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}
