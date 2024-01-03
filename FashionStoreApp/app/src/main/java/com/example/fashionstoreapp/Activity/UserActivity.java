package com.example.fashionstoreapp.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.fashionstoreapp.Model.Order;
import com.example.fashionstoreapp.Model.User;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Retrofit.UserAPI;
import com.example.fashionstoreapp.Somethings.ObjectSharedPreferences;
import com.example.fashionstoreapp.Somethings.PhoneNumberUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    ImageView ivHome, ivUser, ivCart, ivHistory,ivAvatar;

    Button btnEditProfile, btnLogout;

    TextView tvFullName, tvId, tvTotalOrder, tvTotalPrice, tvChangePassword, tvEmail, tvPhone, tvAddress;

    User user;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        AnhXa();
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        appBarClick();
        LoadData();
        btnLogoutClick();
        tvChangePasswordClick();
        btnEditProfileClick();
    }

    private void btnEditProfileClick() {
        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, EditProfileActivity.class));
        });
    }

    private void tvChangePasswordClick() {
        tvChangePassword.setOnClickListener(v -> {
            Dialog dialog = new Dialog(UserActivity.this);
            dialog.setContentView(R.layout.dialog_change_password);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.show();
            //Anh xa dialog
            ConstraintLayout clChangePassword = dialog.findViewById(R.id.clChangePassword);
            ConstraintLayout clChangePasswordSuccess = dialog.findViewById(R.id.clChangePasswordSuccess);
            EditText etOldPassword = dialog.findViewById(R.id.etOldPassword);
            EditText etNewPassword = dialog.findViewById(R.id.etNewPassword);
            EditText etReNewPassword = dialog.findViewById(R.id.etReNewPassword);
            TextView tvErrorChangePassword = dialog.findViewById(R.id.tvErrorChangePassword);
            Button btnChangePassword = dialog.findViewById(R.id.btnChangePassword);
            Button btnOK = dialog.findViewById(R.id.btnOk);
            //====
            btnChangePassword.setOnClickListener(v1 -> {
                String password = etOldPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String reNewPassword =etReNewPassword.getText().toString();
                user = ObjectSharedPreferences.getSavedObjectFromPreference(UserActivity.this, "User", "MODE_PRIVATE", User.class);
                if(password.equals(user.getPassword())){
                    if(newPassword.equals(reNewPassword)){
                        UserAPI.userApi.changePassword(user.getId(), newPassword).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String pass = response.body();
                                if(pass!= null){
                                    clChangePassword.setVisibility(View.GONE);
                                    clChangePasswordSuccess.setVisibility(View.VISIBLE);
                                    user.setPassword(pass);
                                    ObjectSharedPreferences.saveObjectToSharedPreference(UserActivity.this, "User", "MODE_PRIVATE", user);
                                    btnOK.setOnClickListener(v2 -> {
                                        dialog.dismiss();
                                    });
                                }

                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }else {
                        tvErrorChangePassword.setText("New password and confirm password do not match!");
                    }
                } else {
                    tvErrorChangePassword.setText("Your password is not correct");
                }
            });
        });
    }

    private void btnLogoutClick() {
        btnLogout.setOnClickListener(v -> {
            user = ObjectSharedPreferences.getSavedObjectFromPreference(UserActivity.this, "User", "MODE_PRIVATE", User.class);
            if (user.getPassword()==null){
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        finish();
                        ObjectSharedPreferences.saveObjectToSharedPreference(UserActivity.this, "User", "MODE_PRIVATE", null);
                        startActivity(new Intent(UserActivity.this,LoginActivity.class));
                    }
                });
            }
            else{
                ObjectSharedPreferences.saveObjectToSharedPreference(UserActivity.this, "User", "MODE_PRIVATE", null);
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
            }

        });
    }

    private void LoadData() {
        user = ObjectSharedPreferences.getSavedObjectFromPreference(UserActivity.this, "User", "MODE_PRIVATE", User.class);
        UserAPI.userApi.Login(user.getId(), user.getPassword()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                ObjectSharedPreferences.saveObjectToSharedPreference(UserActivity.this, "User", "MODE_PRIVATE", user);
                Glide.with(getApplicationContext()).load(user.getAvatar()).into(ivAvatar);
                tvFullName.setText(user.getUser_Name());
                tvId.setText(user.getId());
                tvTotalOrder.setText(user.getOrder().size()+"");
                int totalPrice = 0;
                for (Order o : user.getOrder()){
                    totalPrice += o.getTotal();
                }
                Locale localeEN = new Locale("en", "EN");
                NumberFormat en = NumberFormat.getInstance(localeEN);
                tvTotalPrice.setText(en.format(totalPrice));

                tvAddress.setText(user.getAddress());
                //tvEmail.setText(user.getEmail());

                String email = user.getEmail();
                String maskedEmail = PhoneNumberUtils.maskEmail(email);
                tvEmail.setText(maskedEmail);


                //tvPhone.setText(user.getPhone_Number());

                String phoneNumber = user.getPhone_Number();
                if(phoneNumber != null){
                    String maskedPhoneNumber = PhoneNumberUtils.maskPhoneNumber(phoneNumber);
                    tvPhone.setText(maskedPhoneNumber);
                }else{
                    tvPhone.setText(" ");
                }







                if(user.getLogin_Type().equals("google")){
                    tvChangePassword.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void appBarClick() {
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, MainActivity.class));
                finish();
            }
        });
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, UserActivity.class));
                finish();
            }
        });
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, CartActivity.class));
                finish();
            }
        });

        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, OrderActivity.class));
                finish();
            }
        });
    }

    private void AnhXa() {
//        tvFullName, tvId, tvTotalOrder, tvTotalPrice, tvChangePassword, tvEmail, tvPhone, tvAddress;
//        Button btnEditProfile, btnLogout;
//
//        ImageView ivAvatar;

        ivHome = findViewById(R.id.ivHome);
        ivUser = findViewById(R.id.ivUser);
        ivCart = findViewById(R.id.ivCart);
        ivHistory = findViewById(R.id.ivHistory);

        tvFullName = findViewById(R.id.tvFullName);
        tvId = findViewById(R.id.tvId);
        tvTotalOrder = findViewById(R.id.tvTotalOrder);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        ivAvatar = findViewById(R.id.ivAvatar);

    }
}
