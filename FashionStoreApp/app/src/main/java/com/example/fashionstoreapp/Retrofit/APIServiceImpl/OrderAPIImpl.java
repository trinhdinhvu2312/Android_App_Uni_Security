package com.example.fashionstoreapp.Retrofit.APIServiceImpl;

import android.content.Context;

import com.example.fashionstoreapp.Model.Order;
import com.example.fashionstoreapp.Retrofit.APIService.OrderAPI;
import com.example.fashionstoreapp.Retrofit.AuthenticatedRetrofitService;

import java.util.List;

import retrofit2.Call;

public class OrderAPIImpl {
    private OrderAPI orderAPI;

    public OrderAPIImpl(Context context) {
        AuthenticatedRetrofitService authenticatedRetrofitService = new AuthenticatedRetrofitService(context);
        orderAPI = authenticatedRetrofitService.getRetrofit().create(OrderAPI.class);
    }

    public Call<Order> placeOrder(String user_id, String fullname, String phoneNumber, String address, String paymentMethod) {
        return orderAPI.placeOrder(user_id, fullname, phoneNumber, address, paymentMethod);
    }

    public Call<List<Order>> getOrderByUserId(String user_id) {
        return orderAPI.getOrderByUserId(user_id);
    }

    public Call<List<Order>> getOrderByUserIdAndPaymentMethod(String user_id, String method) {
        return orderAPI.getOrderByUserIdAndPaymentMethod(user_id, method);
    }
}
