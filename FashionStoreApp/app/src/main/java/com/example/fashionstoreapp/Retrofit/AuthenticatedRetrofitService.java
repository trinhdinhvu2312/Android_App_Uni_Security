package com.example.fashionstoreapp.Retrofit;

import android.content.Context;

import com.example.fashionstoreapp.Interceptor.AccessTokenInterceptor;
import com.example.fashionstoreapp.Interceptor.AuthAuthenticator;
import com.example.fashionstoreapp.Somethings.JwtTokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticatedRetrofitService {

    private String IPAddress="192.168.1.50";

    private Retrofit retrofit = null;

    private JwtTokenManager tokenManager;

    public AuthenticatedRetrofitService(Context context) {
        tokenManager = new JwtTokenManager(context);
        initializeRetrofit(createAuthenticatedOkHttpClient());
    }

    private OkHttpClient createAuthenticatedOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new AccessTokenInterceptor(tokenManager))
                .authenticator(new AuthAuthenticator(tokenManager))
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(getSSLSocketFactory(), new TrustAllCertsManager())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
    }

    Gson gson = new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").setLenient().create();

    private void initializeRetrofit(OkHttpClient okHttpClient) {
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://"+IPAddress+":8080/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new TrustAllCertsManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
