package com.example.fashionstoreapp.Retrofit.APIService;

import com.example.fashionstoreapp.Model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductAPI {

    @GET("newproduct")
    Call<List<Product>> getNewProduct();

    @GET("bestsellers")
    Call<List<Product>> getBestSellers();

    @GET("search")
    Call<List<Product>> search(@Query("searchContent") String searchContent);
}
