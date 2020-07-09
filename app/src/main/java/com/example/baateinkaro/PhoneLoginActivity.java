package com.example.baateinkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {
    private Button sendverificationcodebutton,verifybutton;
    private EditText inputphonenumber,inputverificaioncode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private ProgressDialog loadingbar;
    private PhoneAuthProvider.ForceResendingToken  mResendToken;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        mAuth=FirebaseAuth.getInstance();
        sendverificationcodebutton=findViewById(R.id.send_verification_code);
        verifybutton=findViewById(R.id.verify);
        inputphonenumber=findViewById(R.id.phoneno);
        loadingbar=new ProgressDialog(this);
        inputverificaioncode=findViewById(R.id.verficationcode);
        sendverificationcodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneno=inputphonenumber.getText().toString();
                if(TextUtils.isEmpty(phoneno)){
                    Toast.makeText(PhoneLoginActivity.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingbar.setTitle("Phone verification");
                    loadingbar.setMessage("Please wait...");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneno,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneLoginActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks


                }
            }
        });
        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                          signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                loadingbar.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "Invalid Phone Number!"+e, Toast.LENGTH_SHORT).show();
                sendverificationcodebutton.setVisibility(View.VISIBLE);
                inputphonenumber.setVisibility(View.VISIBLE);
                verifybutton.setVisibility(View.INVISIBLE);
                inputverificaioncode.setVisibility(View.INVISIBLE);

            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {


                mVerificationId = verificationId;
                mResendToken = token;
                loadingbar.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "Code Sent...", Toast.LENGTH_SHORT).show();
                sendverificationcodebutton.setVisibility(View.INVISIBLE);
                inputphonenumber.setVisibility(View.INVISIBLE);
                verifybutton.setVisibility(View.VISIBLE);
                inputverificaioncode.setVisibility(View.VISIBLE);


            }
        };
        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendverificationcodebutton.setVisibility(View.INVISIBLE);
                inputphonenumber.setVisibility(View.INVISIBLE);
                String varifucationcode=inputverificaioncode.getText().toString();
                if(TextUtils.isEmpty(varifucationcode)){
                    Toast.makeText(PhoneLoginActivity.this, "Please write fast...", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingbar.setTitle("Code verification");
                    loadingbar.setMessage("Please wait...");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, varifucationcode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
        }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingbar.dismiss();
                            Toast.makeText(PhoneLoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            SendUserToMainActivity();

                        } else {
                          String message= Objects.requireNonNull(task.getException()).toString();
                            Toast.makeText(PhoneLoginActivity.this, "Error: " +message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(PhoneLoginActivity.this,MainActivity.class);

        startActivity(mainIntent);

    }

    }

