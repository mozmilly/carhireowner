package com.example.carhireowner.ui.notifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carhireowner.MainActivity;
import com.example.carhireowner.R;
import com.example.carhireowner.hiring.BookedCarDetails;
import com.example.carhireowner.hiring.interfaces.HiringInterface;
import com.example.carhireowner.hiring.models.BookedCar;
import com.example.carhireowner.hiring.models.BookedCarAdapter;
import com.example.carhireowner.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment implements BookedCarAdapter.OnItemClicked {
    RecyclerView recyclerView;

    BookedCarAdapter bookedCarAdapter;
    List<BookedCar> bookedCarList;
    private static BookedCar bookedCar;

    public static BookedCar getBookedCar() {
        return bookedCar;
    }

    public static void setBookedCar(BookedCar bookedCar) {
        NotificationsFragment.bookedCar = bookedCar;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = root.findViewById(R.id.booked_cars_recycler);
        HiringInterface hiringInterface = ApiUtils.getHiringService();
        SharedPreferences sp = getContext().getSharedPreferences("pref",0);
        String username = sp.getString("user", "no user");
        hiringInterface.get_booked_cars(username).enqueue(new Callback<List<BookedCar>>() {
            @Override
            public void onResponse(Call<List<BookedCar>> call, Response<List<BookedCar>> response) {
                if (response.code()==200){
                    bookedCarList = response.body();
                    bookedCarAdapter = new BookedCarAdapter(bookedCarList, getContext());
                    GridLayoutManager glm = new GridLayoutManager(getContext().getApplicationContext(), 2, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(glm);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    bookedCarAdapter.setOnClick(NotificationsFragment.this);
                    bookedCarAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(bookedCarAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<BookedCar>> call, Throwable t) {

            }
        });
        return root;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), BookedCarDetails.class);
        setBookedCar(bookedCarList.get(position));
        startActivity(intent);
    }
}