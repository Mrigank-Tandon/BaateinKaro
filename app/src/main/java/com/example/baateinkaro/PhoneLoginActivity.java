package com.example.baateinkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneLoginActivity extends AppCompatActivity {
    private Button sendverificationcodebutton,verifybutton;
    private EditText inputphonenumber,inputverificaioncode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        sendverificationcodebutton=findViewById(R.id.send_verification_code);
        verifybutton=findViewById(R.id.verify);
        inputphonenumber=findViewById(R.id.phoneno);
        inputverificaioncode=findViewById(R.id.verficationcode);
        sendverificationcodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendverificationcodebutton.setVisibility(View.INVISIBLE);
                inputphonenumber.setVisibility(View.INVISIBLE);
                verifybutton.setVisibility(View.VISIBLE);
                inputverificaioncode.setVisibility(View.VISIBLE);
            }
        });

    }
}
