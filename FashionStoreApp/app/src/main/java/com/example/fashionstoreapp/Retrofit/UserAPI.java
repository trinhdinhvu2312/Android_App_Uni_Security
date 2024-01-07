package com.example.fashionstoreapp.Retrofit;

import com.example.fashionstoreapp.Model.LoginResponse;
import com.example.fashionstoreapp.Model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserAPI {

    RetrofitService retrofitService = new RetrofitService();
    UserAPI userApi = retrofitService.getRetrofit().create(UserAPI.class);


    //    @Headers("Accept: application/json; charset=utf-8")

    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("id") String id, @Field("password") String password);

    @GET("getUser")
    Call<User> getUser(@Query("token") String token);

    @POST("checkoldpassword")
    @FormUrlEncoded
    Call<Boolean> checkOldPassword(
            @Field("id") String id,
            @Field("oldPassword") String oldPassword
    );

    @FormUrlEncoded
    @POST("signup")
    Call<User> SignUp(@Field("username") String username, @Field("fullname") String fullname, @Field("email") String email, @Field("password")String password);

    @FormUrlEncoded
    @POST("forgot")
    Call<String> forgotPassword(@Field("id") String user_id);

    @FormUrlEncoded
    @POST("forgotnewpass")
    Call<String> forgotNewPass(@Field("id") String userId, @Field("code") String code, @Field("password") String password);

    @FormUrlEncoded
    @POST("changepassword")
    Call<String> changePassword(@Field("id")String userId, @Field("password") String password);

    @Multipart
    @POST("update")
    Call<User> update(@Part("id") RequestBody userId, @Part MultipartBody.Part avatar, @Part("fullname") RequestBody fullName, @Part("email") RequestBody email,
                      @Part("phoneNumber") RequestBody phoneNumber, @Part("address") RequestBody address);

    @FormUrlEncoded
    @POST("google")
    Call<User> LoginWitGoogle(@Field("id") String userId, @Field("fullname") String fullName, @Field("email") String email, @Field("avatar") String avatar);
}
