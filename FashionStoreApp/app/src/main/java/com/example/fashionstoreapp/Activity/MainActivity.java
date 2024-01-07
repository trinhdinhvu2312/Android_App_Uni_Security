package com.example.fashionstoreapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fashionstoreapp.Adapter.CategoryAdapter;
import com.example.fashionstoreapp.Adapter.ProductAdapter;
import com.example.fashionstoreapp.Model.Category;
import com.example.fashionstoreapp.Model.Product;
import com.example.fashionstoreapp.Model.User;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Retrofit.APIService.CategoryAPI;
import com.example.fashionstoreapp.Retrofit.APIService.ProductAPI;
import com.example.fashionstoreapp.Retrofit.APIService.UserAPI;
import com.example.fashionstoreapp.Somethings.ObjectSharedPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private RecyclerView.Adapter adapter, adapter2, adapter3;

    private RecyclerView recyclerViewCategoryList, recyclerViewNewProductList, recyclerViewBestSellersList;
    TextView tvHiName;
    EditText etSearch;

    ImageView ivAvatar, ivHome, ivUser, ivCart, ivHistory, ivSearch;
    User user;

    //Api

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        appBarClick();
        LoadUserInfo();
        LoadCategories();
        LoadNewProducts();
        LoadBestSellers();
        ivSearchClick();
    }

    private void ivSearchClick() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                intent.putExtra("searchContent", etSearch.getText().toString());
                startActivity(intent);
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                    intent.putExtra("searchContent", etSearch.getText().toString());
                    intent.putExtra("category_id", "-1");
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void appBarClick() {
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        });
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                finish();
            }
        });
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                finish();
            }
        });

        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
                finish();
            }
        });
    }

    private void LoadBestSellers() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewBestSellersList = findViewById(R.id.view3);
        recyclerViewBestSellersList.setLayoutManager(linearLayoutManager);

        //GET API
        ProductAPI.productApi.getBestSellers().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> newProductsList = response.body();
                adapter3 = new ProductAdapter(newProductsList, MainActivity.this);
                recyclerViewBestSellersList.setAdapter(adapter3);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("====", "Call API Get Best Sellers fail");
            }
        });
    }

    private void LoadNewProducts() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewNewProductList = findViewById(R.id.view2);
        recyclerViewNewProductList.setLayoutManager(linearLayoutManager);

        //GET API
        ProductAPI.productApi.getNewProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> newProductsList = response.body();
                adapter2 = new ProductAdapter(newProductsList, MainActivity.this);
                recyclerViewNewProductList.setAdapter(adapter2);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("++++", t.getMessage());
                Log.e("====", "Call API Get New Products fail");
            }
        });
    }

    private void LoadCategories() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.view1);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);
        //Get API
        CategoryAPI.categoryAPI.GetAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                Log.d( "TAGa", "onResponse: " + response.body());
                List<Category> categoriesList = response.body();
                adapter = new CategoryAdapter(categoriesList, MainActivity.this);
                recyclerViewCategoryList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("====", "Call API Get Categories fail");

            }
        });
    }

    private void LoadUserInfo() {
        // Get the token from SharedPreferences
        String token = ObjectSharedPreferences.getStringFromPreference(MainActivity.this, "Token", "MODE_PRIVATE");

        // Check if the token is not empty
        if (token != null && !token.isEmpty()) {
            // Call the API to get user information
            UserAPI.userApi.getUser(token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        user = response.body();
                        // Save the user information to SharedPreferences for future use if needed
                        ObjectSharedPreferences.saveObjectToSharedPreference(MainActivity.this, "User", "MODE_PRIVATE", user);

                        // Update UI with user information
                        tvHiName.setText("Hi " + user.getUser_Name());
                        Glide.with(getApplicationContext()).load(user.getAvatar()).into(ivAvatar);
                    } else {
                        // Handle API error response
                        // You may want to show an error message or take appropriate action
                        Log.e("TAG", "API getUser failed");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Handle API call failure
                    // You may want to show an error message or take appropriate action
                    Log.e("TAG", "API getUser failed", t);
                }
            });
        } else {
            // Handle the case where the token is empty or null
            // You may want to show an error message or take appropriate action
            Log.e("TAG", "Token is empty or null");
        }
    }


    private void AnhXa() {
        tvHiName = findViewById(R.id.tvHiName);
        ivAvatar = findViewById(R.id.ivAvatar);
        ivHome = findViewById(R.id.ivHome);
        ivUser = findViewById(R.id.ivUser);
        ivCart = findViewById(R.id.ivCart);
        ivHistory = findViewById(R.id.ivHistory);
        etSearch = findViewById(R.id.etSearch);
        ivSearch = findViewById(R.id.ivSearch);
    }
}