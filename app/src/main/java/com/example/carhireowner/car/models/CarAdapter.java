package com.example.carhireowner.car.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carhireowner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarAdapter extends  RecyclerView.Adapter<CarAdapter.MyViewHolder>{

    List<Car> carList;
    Context context;

    private boolean isLoaderVisible = false;

    public CarAdapter(List<Car> carList, Context context) {
        this.carList = carList;
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
                .inflate(R.layout.car_rows, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.price.setText(("Price Per Day: "+carList.get(position).getPrice_per_day()));
        holder.carNumberPlate.setText(("Number Plate: "+carList.get(position).getNumber_plate()));
        holder.seaters.setText(("Seaters: "+carList.get(position).getSeaters()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(position);
            }
        });

        if (carList.get(position).getPhoto()!=null){
            if (URLUtil.isValidUrl("https://carhiremodule.pythonanywhere.com"+carList.get(position).getPhoto())){
                Picasso.with(context)
                        .load("https://carhiremodule.pythonanywhere.com"+carList.get(position).getPhoto())
                        .placeholder(R.drawable.cab)
                        .into(holder.imageView);
            }
        }


    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView price, seaters, carNumberPlate;
        Button edit;
        ImageView imageView;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            price = view.findViewById(R.id.car_row_price);
            carNumberPlate = view.findViewById(R.id.car_number_plate);
            seaters = view.findViewById(R.id.car_row_seaters);
            imageView = view.findViewById(R.id.car_row_image);

            parent = view.findViewById(R.id.car_row_parent);
        }
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateList(List<Car> list){
        carList = list;
        notifyDataSetChanged();
    }

    public void addItems(List<Car> postItems) {
        carList.addAll(postItems);
        notifyDataSetChanged();
    }
    public void addLoading() {
        isLoaderVisible = true;

    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = carList.size() - 1;
        Car item = getItem(position);
        if (item != null) {
            carList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        carList.clear();
        notifyDataSetChanged();
    }

    Car getItem(int position) {
        return carList.get(position);
    }

}
