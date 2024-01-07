package com.example.fashionstoreapp.Retrofit.APIServiceImpl;

import android.content.Context;

import com.example.fashionstoreapp.Model.Cart;
import com.example.fashionstoreapp.Retrofit.APIService.CartAPI;
import com.example.fashionstoreapp.Retrofit.AuthenticatedRetrofitService;

import java.util.List;

import retrofit2.Call;

public class CartAPIImpl {

    private CartAPI cartAPI;

    public CartAPIImpl(Context context) {
        AuthenticatedRetrofitService authenticatedRetrofitService = new AuthenticatedRetrofitService(context);
        cartAPI = authenticatedRetrofitService.getRetrofit().create(CartAPI.class);
    }

    public Call<Cart> addToCart(String user_id, int product_id, int count) {
        return cartAPI.addToCart(user_id, product_id, count);
    }

    public Call<List<Cart>> cartOfUser(String userId) {
        return cartAPI.cartOfUser(userId);
    }

    public Call<String> deleteCart(int cart_id, String user_id) {
        return cartAPI.deleteCart(cart_id, user_id);
    }
}
