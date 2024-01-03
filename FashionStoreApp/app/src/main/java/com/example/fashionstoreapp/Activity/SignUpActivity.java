package com.example.fashionstoreapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fashionstoreapp.Model.User;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Retrofit.UserAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText etUserName, etPassword, etEmail, etRePassword, etFullName;
    Button btnSignUp;

    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        AnhXa();
        btnSignUpClick();
        tvLoginClick();
    }

    private void tvLoginClick() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private void btnSignUpClick() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUserName = findViewById(R.id.etUserName);
                etFullName = findViewById(R.id.etNewPass);
                etEmail = findViewById(R.id.etEmail);
                etPassword = findViewById(R.id.etPassword);
                etRePassword = findViewById(R.id.etRePassword);

                if (TextUtils.isEmpty(etUserName.getText().toString())){
                    etUserName.setError("Please enter your username");
                    etUserName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(etFullName.getText().toString())){
                    etFullName.setError("Please enter your full name");
                    etFullName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(etEmail.getText().toString())){
                    etEmail.setError("Please enter your email");
                    etEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(etPassword.getText().toString())){
                    etPassword.setError("Please enter your username");
                    etPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(etRePassword.getText().toString())){
                    etRePassword.setError("Please enter your password");
                    etRePassword.requestFocus();
                    return;
                }

                // Kiểm tra xem mật khẩu đáp ứng các yêu cầu hay không
                if (!isValidPassword(etPassword.getText().toString())) {
                    etPassword.setError("Password must be at least 8 characters long and contain at least 1 uppercase letter and 1 special character");
                    etPassword.requestFocus();
                    return;
                }

                if (!etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                    etRePassword.setError("Password and RePassword do not match");
                    etRePassword.requestFocus();
                    return;
                }

                if (!isValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Please enter a valid email address");
                    etEmail.requestFocus();
                    return;
                }

                String username = etUserName.getText().toString();
                String fullname = etFullName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String repassword = etRePassword.getText().toString();

                if (!password.equals(repassword)){
                    etRePassword.setError("Password and RePassword not match");
                    etRePassword.requestFocus();
                    return;
                }
                else {

                    UserAPI.userApi.SignUp(username, fullname, email, password).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            Log.e("ffff", "Thành công");
                            Log.e("ffff", user.toString());
                            Toast.makeText(SignUpActivity.this,"SignUp Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("ffff", "Kết nối API thất bại");

                        }
                    });
                }
            }
        });
    }

    private boolean isValidPassword(String password) {
        // Kiểm tra độ dài của mật khẩu
        if (password.length() < 8) {
            return false;
        }

        // Kiểm tra xem mật khẩu có chứa ít nhất 1 chữ cái hoa không
        boolean containsUpperCase = containsUpperCase(password);

        // Kiểm tra xem mật khẩu có chứa ít nhất 1 ký tự đặc biệt không
        boolean containsSpecialCharacter = containsSpecialCharacter(password);

        return containsUpperCase && containsSpecialCharacter;
    }

    // Phương thức kiểm tra xem một chuỗi có chứa ít nhất 1 chữ cái hoa không
    private boolean containsUpperCase(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        // Sử dụng biểu thức chính quy từ Patterns để kiểm tra địa chỉ email
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // Phương thức kiểm tra xem một chuỗi có chứa ít nhất 1 ký tự đặc biệt không
    private boolean containsSpecialCharacter(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
        }
        return false;
    }
    private void AnhXa() {
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin1);
    }


}
