package com.example.fashionstoreapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fashionstoreapp.Model.Address;
import com.example.fashionstoreapp.R;
import com.example.fashionstoreapp.Somethings.AESEncryption;
import com.example.fashionstoreapp.Somethings.ObjectSharedPreferences;

public class AddressActivity extends AppCompatActivity {
    ImageView ivBack;
    EditText etFullName, etPhoneNumber, etAddress;

    Button saveAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        AnhXa();
        ivBackClick();
        LoadInformation();
        saveAddressClick();
    }

    private void saveAddressClick() {
        saveAddress.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etFullName.getText().toString())){
                Toast.makeText(AddressActivity.this.getApplicationContext(), "Please enter your full name", Toast.LENGTH_SHORT).show();
                etFullName.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(etPhoneNumber.getText().toString())){
                Toast.makeText(AddressActivity.this.getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                etPhoneNumber.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(etAddress.getText().toString())){
                Toast.makeText(AddressActivity.this.getApplicationContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                etAddress.requestFocus();
                return;
            }
//            String phone1 = etPhoneNumber.getText().toString();
//            String encryptPhoneNumber = AESEncryption.encrypt(phone1, String.valueOf( etFullName ) );

            Address address = new Address(etFullName.getText().toString(), etPhoneNumber.getText().toString(), etAddress.getText().toString());
            ObjectSharedPreferences.saveObjectToSharedPreference(AddressActivity.this, "address", "MODE_PRIVATE", address);
            startActivity(new Intent(AddressActivity.this, CheckOutActivity.class));
            finish();
        });
    }

    private void LoadInformation() {
        Address address = ObjectSharedPreferences.getSavedObjectFromPreference(AddressActivity.this, "address", "MODE_PRIVATE", Address.class);
        if(address != null){
            etFullName.setText(address.getFullName());

            etPhoneNumber.setText(address.getPhoneNumber());
//            String phone1 = address.getPhoneNumber();
//            String decryptPhone = AESEncryption.decrypt(phone1, address.getFullName() );
//            etPhoneNumber.setText(decryptPhone);

            etAddress.setText(address.getAddress());
        }
    }

    private void ivBackClick() {
        ivBack.setOnClickListener(v -> {
            startActivity(new Intent(AddressActivity.this, CheckOutActivity.class));
        });

    }

    private void AnhXa() {
        ivBack = findViewById(R.id.ivBack);
        etFullName = findViewById(R.id.etNewPass);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        saveAddress = findViewById(R.id.saveAddress);
    }
}
