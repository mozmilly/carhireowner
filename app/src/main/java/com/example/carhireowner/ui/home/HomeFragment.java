package com.example.carhireowner.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carhireowner.R;
import com.example.carhireowner.car.ViewCarDetailsActivity;
import com.example.carhireowner.car.interfaces.CarInterface;
import com.example.carhireowner.car.models.Car;
import com.example.carhireowner.car.models.CarAdapter;
import com.example.carhireowner.utils.ApiUtils;
import com.example.carhireowner.utils.PaginationListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.carhireowner.utils.PaginationListener.PAGE_START;

public class HomeFragment extends Fragment implements CarAdapter.OnItemClicked {

    RecyclerView recyclerView;
    CarAdapter carAdapter;
    List<Car> carList;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;


    private static Car car;

    public static Car getCar() {
        return car;
    }

    public static void setCar(Car car) {
        HomeFragment.car = car;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.my_cars_recycler);

        CarInterface carInterface = ApiUtils.getCarService();
        SharedPreferences sp = getContext().getSharedPreferences("pref",0);
        String username = sp.getString("user", "no user");
        carInterface.get_my_cars(username, currentPage).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.code()==200){
                    carList = response.body();
                    carAdapter = new CarAdapter(carList, getContext());
                    GridLayoutManager glm = new GridLayoutManager(getContext().getApplicationContext(), 2, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(glm);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    carAdapter.setOnClick(HomeFragment.this);
                    carAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(carAdapter);

                    recyclerView.addOnScrollListener(new PaginationListener(glm){

                        @Override
                        protected void loadMoreItems() {
                            isLoading = true;
                            currentPage++;
                            doApiCall(root);
                        }

                        @Override
                        public boolean isLastPage() {
                            return false;
                        }

                        @Override
                        public boolean isLoading() {
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {

            }
        });

        return root;
    }


    private void doApiCall(View root) {
        final List<Car> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar = root.findViewById(R.id.this_progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                CarInterface carInterface = ApiUtils.getCarService();
                SharedPreferences sp = getContext().getSharedPreferences("pref",0);
                String username = sp.getString("user", "no user");
                carInterface.get_my_cars(username, currentPage).enqueue(new Callback<List<Car>>() {
                    @Override
                    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                        if (response.code()==200){
                            items.addAll(response.body());

                            /**
                             * manage progress view
                             */
                            if (currentPage != PAGE_START) carAdapter.removeLoading();
                            carAdapter.addItems(items);

                            // check weather is last page or not
                            if (response.body().size()==30){
                                carAdapter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;
                            progressBar.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Car>> call, Throwable t) {

                    }
                });


            }
        }, 1500);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ViewCarDetailsActivity.class);
        setCar(carList.get(position));
        startActivity(intent);
    }
}