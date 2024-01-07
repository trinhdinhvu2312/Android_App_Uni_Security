package com.example.fashionstoreapp.Retrofit.APIServiceImpl;

import android.content.Context;

import com.example.fashionstoreapp.Model.Category;
import com.example.fashionstoreapp.Retrofit.APIService.CategoryAPI;
import com.example.fashionstoreapp.Retrofit.AuthenticatedRetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public class CategoryAPIImpl {
    private CategoryAPI categoryAPI;

    public CategoryAPIImpl(Context context) {
        AuthenticatedRetrofitService authenticatedRetrofitService = new AuthenticatedRetrofitService(context);
        categoryAPI = authenticatedRetrofitService.getRetrofit().create(CategoryAPI.class);
    }

    public  Call<List<Category>> GetAllCategories() {
        return categoryAPI.GetAllCategories();
    }
}
