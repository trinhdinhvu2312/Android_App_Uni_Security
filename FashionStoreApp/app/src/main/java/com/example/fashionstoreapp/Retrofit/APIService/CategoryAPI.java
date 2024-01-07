package com.example.fashionstoreapp.Retrofit.APIService;

import com.example.fashionstoreapp.Model.Category;
import com.example.fashionstoreapp.Retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryAPI {
    @GET("category")
    Call<List<Category>> GetAllCategories();
}
