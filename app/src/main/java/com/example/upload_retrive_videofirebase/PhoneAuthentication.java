package com.example.upload_retrive_videofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneAuthentication extends AppCompatActivity {

    EditText enterMobileNumber;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        enterMobileNumber = findViewById(R.id.phn_veri);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobile_number = enterMobileNumber.getText().toString();

                if(mobile_number.isEmpty()){
                    enterMobileNumber.setError("Please Enter the Phone Number!");
                }
                else {
                    Intent intent = new Intent(PhoneAuthentication.this,VerifyOTP.class);
                    intent.putExtra("mobile",mobile_number);
                    startActivity(intent);
                }
            }
        });
    }
}