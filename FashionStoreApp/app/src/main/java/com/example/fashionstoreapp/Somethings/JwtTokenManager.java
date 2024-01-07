package com.example.fashionstoreapp.Somethings;

import android.content.Context;
import android.content.SharedPreferences;

public class JwtTokenManager {

    private static final String SHARED_PREF_NAME = "jwt_token_pref";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private SharedPreferences sharedPreferences;

    public JwtTokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.apply();
    }
}
