package com.example.fashionstoreapp.Interceptor;

import android.content.Context;
import android.content.Intent;

import com.example.fashionstoreapp.Activity.LoginActivity;
import com.example.fashionstoreapp.Model.LoginResponse;
import com.example.fashionstoreapp.Retrofit.APIService.UserAPI;
import com.example.fashionstoreapp.Somethings.JwtTokenManager;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

public class AuthAuthenticator implements Authenticator {

    private JwtTokenManager tokenManager;
    private UserAPI userAPI;
    private Context context;

    public AuthAuthenticator(JwtTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public AuthAuthenticator(Context context, JwtTokenManager tokenManager, UserAPI userAPI) {
        this.context = context;
        this.tokenManager = tokenManager;
        this.userAPI = userAPI;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // Gọi API để refresh token
        String token = tokenManager.getToken();
        Call<LoginResponse> call = userAPI.refreshToken(token);

        try {
            retrofit2.Response<LoginResponse> refreshTokenResponse = call.execute();

            if (refreshTokenResponse.isSuccessful()) {
                LoginResponse loginResponse = refreshTokenResponse.body();
                if (loginResponse != null) {
                    tokenManager.saveToken(loginResponse.getToken());
                    return response
                            .request()
                            .newBuilder()
                            .header("Authorization", "Bearer " + loginResponse.getToken())
                            .build();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Nếu không thành công, chuyển đến màn hình đăng nhập
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);

        // Đặt response cho interceptor để ngăn cản retry và thông báo lỗi về phía ứng dụng.
        return null;
    }
}
