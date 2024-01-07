package com.example.fashionstoreapp.Retrofit.APIService;

import com.example.fashionstoreapp.Model.Order;
import com.example.fashionstoreapp.Retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderAPI {
    @FormUrlEncoded
    @POST("placeorder")
    Call<Order> placeOrder(@Field("user_id") String user_id, @Field("fullname") String fullname,
                           @Field("phoneNumber") String phoneNumber, @Field("address") String address, @Field("paymentMethod") String paymentMethod);

    @GET("order")
    Call<List<Order>> getOrderByUserId(@Query("user_id") String user_id);

    @GET("ordermethod")
    Call<List<Order>> getOrderByUserIdAndPaymentMethod(@Query("user_id") String user_id, @Query("method") String method);
}
