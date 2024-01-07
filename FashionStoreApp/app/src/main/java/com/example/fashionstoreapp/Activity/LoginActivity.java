package com.example.fashionstoreapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fashionstoreapp.Model.Address;
import com.example.fashionstoreapp.Model.LoginResponse;
import com.example.fashionstoreapp.Model.User;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Retrofit.APIService.UserAPI;
import com.example.fashionstoreapp.Somethings.JwtTokenManager;
import com.example.fashionstoreapp.Somethings.ObjectSharedPreferences;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etPassword, etUserName;
    Button btnLogin;
    TextView tvRegister, tvForgotPassword, tvAdmin;
    User user = new User();
    ProgressBar progressBar;
    ConstraintLayout clGoogle;
    private JwtTokenManager tokenManager;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();
//        progressBar.setVisibility(View.VISIBLE);
        btnLoginClick();
        tvRegisterClick();
        tvForgotPasswordClick();
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        tokenManager = new JwtTokenManager(this);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            navigateToSecondActivity();
        }
        clGoogleClick();
        tvAdminClick();
    }

    private void tvAdminClick() {
        tvAdmin.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
        });
    }

    private void clGoogleClick() {
        clGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent,1000);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }
    void navigateToSecondActivity(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String id = acct.getId();
            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String avatar = String.valueOf(acct.getPhotoUrl());
            UserAPI.userApi.LoginWitGoogle(id, name, email, avatar).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    user = response.body();
                    if (user!=null){
                        Toast.makeText(LoginActivity.this,"Login Successfully", Toast.LENGTH_LONG).show();
                        ObjectSharedPreferences.saveObjectToSharedPreference(LoginActivity.this, "User", "MODE_PRIVATE", user);
                        if(user.getAddress()!=null && user.getPhone_Number()!=null){
                            Address address = new Address(user.getUser_Name(), user.getPhone_Number(), user.getAddress());
                            ObjectSharedPreferences.saveObjectToSharedPreference(LoginActivity.this, "address", "MODE_PRIVATE", address);
                        }

                        Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("object", user);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Incorrect UserName or Password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    private void tvForgotPasswordClick() {
        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

    }

    private void tvRegisterClick() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void btnLoginClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login() {
        etPassword = findViewById(R.id.etPassword);
        etUserName = findViewById(R.id.etUserName);

        if (TextUtils.isEmpty(etUserName.getText().toString())) {
            etUserName.setError("Please enter your username");
            etUserName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }

        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();

        UserAPI.userApi.login(username, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        String token = loginResponse.getToken();
                        tokenManager.saveToken(token);
                        // Save the token to SharedPreferences
                        ObjectSharedPreferences.saveStringToSharedPreference(LoginActivity.this, "Token", "MODE_PRIVATE", token);

                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to connect, try again later", Toast.LENGTH_LONG).show();
                    Log.e("TAG", "API Login failed");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failed to connect, try again later", Toast.LENGTH_LONG).show();
                Log.e("TAG", "API Login failed", t);
            }
        });
    }


    private void anhXa() {
        btnLogin = findViewById(R.id.btnSignUp);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBar = findViewById(R.id.progressBar);
        clGoogle = findViewById(R.id.clGoogle);
        tvAdmin = findViewById(R.id.tvAdmin);
    }
}
