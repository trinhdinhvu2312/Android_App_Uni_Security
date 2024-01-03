package com.example.fashionstoreapp.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionstoreapp.Adapter.CheckOutAdapter;
import com.example.fashionstoreapp.Model.Address;
import com.example.fashionstoreapp.Model.Cart;
import com.example.fashionstoreapp.Model.Order;
import com.example.fashionstoreapp.Model.User;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Retrofit.CartAPI;
import com.example.fashionstoreapp.Retrofit.OrderAPI;
import com.example.fashionstoreapp.Somethings.ObjectSharedPreferences;
import com.example.fashionstoreapp.Somethings.PhoneNumberUtils;
import com.example.fashionstoreapp.Zalo.Api.CreateOrder;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {

    ImageView ivBack;
    TextView tvUserName, tvAddress, tvTotalPrice, tvPhoneNumber, tvChangeAddress, tvAddAddress, tvPayWithZaloPay, tvPayOnDelivery;
    Button btnPlaceOrder;
    ConstraintLayout constraintLayoutAddress, constraintLayoutNotAddress, placeOrder, placeOrderSuccess;

    RadioButton rbPayOnDelivery, rbPayWithZaloPay;

    RecyclerView.Adapter checkOutAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        AnhXa();
        LoadAddress();
        LoadProductItem();
        ivBackClick();
        constraintLayoutNotAddressClick();
        tvChangeAddressClick();
        btnPlaceOrderClick();
        radioButtonClick();
    }

    private void radioButtonClick() {
        tvPayOnDelivery.setOnClickListener(v -> {
            rbPayOnDelivery.setChecked(true);
        });
        tvPayWithZaloPay.setOnClickListener(v -> {
            rbPayWithZaloPay.setChecked(true);
        });
    }

    private void btnPlaceOrderClick() {
        btnPlaceOrder.setOnClickListener(v -> {
            if (rbPayOnDelivery.isChecked()) {
                newOrder("Pay on Delivery");
            } else if (rbPayWithZaloPay.isChecked()) {
                StrictMode.ThreadPolicy policy = new
                        StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                // ZaloPay SDK Init
//                ZaloPaySDK.init(2553, Environment.SANDBOX);
                CreateOrder orderApi = new CreateOrder();

                try {
                    String amount = tvTotalPrice.getText().toString().replace(",","");
                    JSONObject data = orderApi.createOrder(amount);
                    Log.e("Amount", amount);
                    String code = data.getString("return_code");

                    if (code.equals("1")) {
                        Dialog dialog = new Dialog(CheckOutActivity.this);
                        String token = data.getString("zp_trans_token");
                        ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                newOrder("Pay with ZaloPay");
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                                dialog.setContentView(R.layout.dialog_pay_with_zalopay);
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();

                                Button btnOk = dialog.findViewById(R.id.btnOk);
                                btnOk.setOnClickListener(v1 -> {
                                    dialog.dismiss();
                                });
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                dialog.setContentView(R.layout.dialog_pay_with_zalopay);
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                                Button btnOk = dialog.findViewById(R.id.btnOk);
                                btnOk.setOnClickListener(v1 -> {
                                    dialog.dismiss();
                                });
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Please choose a payment method", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void newOrder(String method) {
        Address address = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "address", "MODE_PRIVATE", Address.class);
        if (address == null) {
            Toast.makeText(CheckOutActivity.this.getApplicationContext(), "Please add your address before place order", Toast.LENGTH_SHORT).show();
        } else {
            User user = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "User", "MODE_PRIVATE", User.class);
            OrderAPI.orderAPI.placeOrder(user.getId(), tvUserName.getText().toString(), tvPhoneNumber.getText().toString().replace("(", "").replace(")", ""), tvAddress.getText().toString(), method).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    Order order = response.body();
                    if (order != null) {
                        placeOrder.setVisibility(View.GONE);
                        placeOrderSuccess.setVisibility(View.VISIBLE);
                        Button btnContinueShopping = findViewById(R.id.btnContinueShopping);
                        btnContinueShopping.setOnClickListener(v1 -> {
                            placeOrder.setVisibility(View.VISIBLE);
                            placeOrderSuccess.setVisibility(View.GONE);
                            startActivity(new Intent(CheckOutActivity.this, MainActivity.class));
                        });
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {

                }
            });
        }
    }

    private void tvChangeAddressClick() {
        tvChangeAddress.setOnClickListener(v -> {
            startActivity(new Intent(CheckOutActivity.this, AddressActivity.class));
            finish();
        });
    }

    private void constraintLayoutNotAddressClick() {
        constraintLayoutNotAddress.setOnClickListener(v -> {
            startActivity(new Intent(CheckOutActivity.this, AddressActivity.class));
            finish();
        });
    }

    private void ivBackClick() {
        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void LoadProductItem() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.view);
        recyclerView.setLayoutManager(linearLayoutManager);
        User user = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "User", "MODE_PRIVATE", User.class);
        CartAPI.cartAPI.cartOfUser(user.getId()).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                List<Cart> listCart = response.body();
//                User user = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "User", "MODE_PRIVATE", User.class);

                checkOutAdapter = new CheckOutAdapter(listCart, CheckOutActivity.this);
                recyclerView.setAdapter(checkOutAdapter);

                //load total price
                int Total = 0;
                for (Cart y : listCart) {
                    Total += y.getCount() * y.getProduct().getPrice();
                }
                Locale localeEN = new Locale("en", "EN");
                NumberFormat en = NumberFormat.getInstance(localeEN);
                tvTotalPrice.setText(en.format(Total));
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("====", "Call API Cart of user fail");
            }
        });
    }

    private void LoadAddress() {
        Address address = ObjectSharedPreferences.getSavedObjectFromPreference(CheckOutActivity.this, "address", "MODE_PRIVATE", Address.class);
//        Log.e("====", address.toString());
        if (address == null) {
            constraintLayoutAddress.setVisibility(View.GONE);
            constraintLayoutNotAddress.setVisibility(View.VISIBLE);
        } else {
            //tvPhoneNumber.setText("(" + address.getPhoneNumber() + ")");
            String phoneNumber = address.getPhoneNumber();
            String maskedPhoneNumber = PhoneNumberUtils.maskPhoneNumber(phoneNumber);
            tvPhoneNumber.setText("("+ maskedPhoneNumber+")");


            tvUserName.setText(address.getFullName());
            tvAddress.setText(address.getAddress());
        }
    }

    private void AnhXa() {
        ivBack = findViewById(R.id.ivBack);
        tvUserName = findViewById(R.id.tvUserName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvAddress = findViewById(R.id.tvAddress);
        tvChangeAddress = findViewById(R.id.tvChangeAddress);
        tvAddAddress = findViewById(R.id.tvAddAddress);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        constraintLayoutAddress = findViewById(R.id.constraintLayoutAddress);
        constraintLayoutNotAddress = findViewById(R.id.constraintLayoutNotAddress);
        placeOrderSuccess = findViewById(R.id.placeOrderSuccess);
        placeOrder = findViewById(R.id.placeOrder);
        tvPayWithZaloPay = findViewById(R.id.tvPayWithZaloPay);
        tvPayOnDelivery = findViewById(R.id.tvPayOnDelivery);
        rbPayWithZaloPay = findViewById(R.id.rbPayWithZaloPay);
        rbPayOnDelivery = findViewById(R.id.rbPayOnDelivery);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}