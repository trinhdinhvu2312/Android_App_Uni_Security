package com.example.fashionstoreapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fashionstoreapp.Model.User;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Somethings.ObjectSharedPreferences;

public class IntroActivity extends AppCompatActivity {
    TextView tvStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        AnhXa();
        tvStartClick();
    }

    private void tvStartClick() {
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User isLoged = ObjectSharedPreferences.getSavedObjectFromPreference(IntroActivity.this, "User", "MODE_PRIVATE", User.class);
//        Log.e("loged", isLoged.toString());
                if (isLoged!=null){
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));

                }
            }
        });
    }

    private void AnhXa() {
        tvStart = findViewById(R.id.tvStart);
    }
}
