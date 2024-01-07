package com.example.fashionstoreapp.Retrofit;

import com.example.fashionstoreapp.Model.Category;
import com.example.fashionstoreapp.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategoryAPI {
    RetrofitService retrofitService = new RetrofitService();
    CategoryAPI categoryAPI = retrofitService.getRetrofit().create(CategoryAPI.class);
    @GET("category")
    Call<List<Category>> GetAllCategories();
}
