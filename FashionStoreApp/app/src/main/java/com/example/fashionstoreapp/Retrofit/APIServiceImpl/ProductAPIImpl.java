package com.example.fashionstoreapp.Retrofit.APIServiceImpl;

import android.content.Context;

import com.example.fashionstoreapp.Model.Product;
import com.example.fashionstoreapp.Retrofit.APIService.ProductAPI;
import com.example.fashionstoreapp.Retrofit.AuthenticatedRetrofitService;

import java.util.List;

import retrofit2.Call;

public class ProductAPIImpl {

    private ProductAPI productApi;

    public ProductAPIImpl(Context context) {
        AuthenticatedRetrofitService authenticatedRetrofitService = new AuthenticatedRetrofitService(context);
        productApi = authenticatedRetrofitService.getRetrofit().create(ProductAPI.class);
    }

    public Call<List<Product>> getNewProduct() {
        return productApi.getNewProduct();
    }

    public Call<List<Product>> getBestSellers() {
        return productApi.getBestSellers();
    }

    public Call<List<Product>> search(String searchContent) {
        return productApi.search(searchContent);
    }
}
