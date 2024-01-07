package com.example.fashionstoreapp.Activity;

import static java.lang.Integer.parseInt;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fashionstoreapp.Adapter.SliderAdapter;
import com.example.fashionstoreapp.Model.Cart;
import com.example.fashionstoreapp.Model.Product;
import com.example.fashionstoreapp.Model.User;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Retrofit.APIService.CartAPI;
import com.example.fashionstoreapp.Retrofit.APIServiceImpl.CartAPIImpl;
import com.example.fashionstoreapp.Somethings.ObjectSharedPreferences;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailActivity extends AppCompatActivity {
    TextView tvTitle, tvdescription, tvPrice, tvTotalPrice, tvSold, tvAvailable, tvNumber, tvAddToCart;
    ImageView ivMinus, ivPlus;
    Product product;

    ConstraintLayout clBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        product = (Product) getIntent().getSerializableExtra("product");
        AnhXa();
        LoadProduct();
        ivMinusClick();
        ivPlusClick();
        tvAddToCartClick();
        clBackClick();
    }

    private void clBackClick() {
        clBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void tvAddToCartClick() {
        tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = ObjectSharedPreferences.getSavedObjectFromPreference(ShowDetailActivity.this, "User", "MODE_PRIVATE", User.class);
                int count = parseInt(tvNumber.getText().toString());
                CartAPIImpl cartAPIImpl = new CartAPIImpl(ShowDetailActivity.this);
                cartAPIImpl.addToCart(user.getId(), product.getId(), count).enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        Cart cart = response.body();
                        if(cart !=null){
                            Toast.makeText(ShowDetailActivity.this.getApplicationContext(), "Thêm vào giỏ thành công", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Toast.makeText(ShowDetailActivity.this.getApplicationContext(), "Thêm vào giỏ thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void LoadProduct() {
        LoadImage();
        tvTitle.setText(product.getProduct_Name());
        tvdescription.setText(product.getDescription());
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        tvPrice.setText(en.format(product.getPrice()));
        tvSold.setText(String.valueOf(product.getSold()));
        tvAvailable.setText(String.valueOf(product.getQuantity()));
        tvTotalPrice.setText(en.format(product.getPrice()));
        tvNumber.setText("1");
        
    }

    private void LoadImage() {
        SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapter adapter = new SliderAdapter(ShowDetailActivity.this, product.getProductImage());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    private void ivPlusClick() {
        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = parseInt(tvNumber.getText().toString()) +1;
                if(number <= product.getQuantity()){
                    tvNumber.setText(String.valueOf(number));
                    Locale localeEN = new Locale("en", "EN");
                    NumberFormat en = NumberFormat.getInstance(localeEN);
                    tvTotalPrice.setText(en.format(product.getPrice()*number));
                }
            }
        });
    }

    private void ivMinusClick() {
        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = parseInt(tvNumber.getText().toString()) - 1;
                if(number >= 1){
                    tvNumber.setText(String.valueOf(number));
                    Locale localeEN = new Locale("en", "EN");
                    NumberFormat en = NumberFormat.getInstance(localeEN);
                    tvTotalPrice.setText(en.format(product.getPrice()*number));
                }
            }
        });
    }

    private void AnhXa() {
        tvAddToCart = findViewById(R.id.tvAddToCart);
        tvTitle = findViewById(R.id.tvTitle);
        tvdescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvSold = findViewById(R.id.tvSold);
        tvAvailable = findViewById(R.id.tvAvailable);
        tvNumber = findViewById(R.id.tvNumber);
        ivMinus = findViewById(R.id.ivMinus);
        ivPlus = findViewById(R.id.ivPlus);
        clBack = findViewById(R.id.clBack);
    }
}
