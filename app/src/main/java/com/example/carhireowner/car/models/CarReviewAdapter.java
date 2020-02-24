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

public class CarReviewAdapter extends  RecyclerView.Adapter<CarReviewAdapter.MyViewHolder>{

    List<CarReview> carReviewList;
    Context context;

    private boolean isLoaderVisible = false;



    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_review_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(("Name: "+carReviewList.get(position).getUser().getFirst_name()
                +" "+carReviewList.get(position).getUser().getLast_name()));
        holder.comment.setText(("Comment: "+carReviewList.get(position).getComment()));


    }

    @Override
    public int getItemCount() {
        return carReviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, comment;

        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            name = view.findViewById(R.id.car_review_user_name);
            comment = view.findViewById(R.id.car_review_comment);
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

    public void updateList(List<CarReview> list){
        carReviewList = list;
        notifyDataSetChanged();
    }

    public void addItems(List<CarReview> postItems) {
        carReviewList.addAll(postItems);
        notifyDataSetChanged();
    }
    public void addLoading() {
        isLoaderVisible = true;

    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = carReviewList.size() - 1;
        CarReview item = getItem(position);
        if (item != null) {
            carReviewList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        carReviewList.clear();
        notifyDataSetChanged();
    }

    CarReview getItem(int position) {
        return carReviewList.get(position);
    }

}
