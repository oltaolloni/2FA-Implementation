package com.example.implementimi2fa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import javax.mail.MessagingException;

public class VerifyActivity extends AppCompatActivity {
    private EditText otpET;
    private TextView resendBtn;
    private boolean resendEnable = false;
    private int resendTime = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        otpET = findViewById(R.id.otpET);
        resendBtn = findViewById(R.id.resendBtn);
        final TextView otpEmail = findViewById(R.id.otpEmail);
        final Button verifyBtn = findViewById(R.id.verifyBtn);

        final String email = getIntent().getStringExtra("email");
        otpEmail.setText(email);


        String generatedOtp = getIntent().getStringExtra("otp");

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = otpET.getText().toString();
                verifyOtp(enteredOtp, generatedOtp);
            }
        });

        startCountDownTimer();

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendEnable) {
                 //Resend code logic
                }
            }
        });
    }

    private void verifyOtp(String enteredOtp, String generatedOtp){
        if (enteredOtp.equals(generatedOtp)) {
            Toast.makeText(VerifyActivity.this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(VerifyActivity.this, SuccessActivity.class));
        } else {
            Toast.makeText(VerifyActivity.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
    private void startCountDownTimer(){
        resendEnable = false;
        resendBtn.setTextColor(Color.parseColor("#99000000"));
        new CountDownTimer(resendTime * 1000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                resendBtn.setText("Resend Code (" + (millisUntilFinished / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnable = true;
                resendBtn.setText("Resend Code");
                resendBtn.setTextColor(getResources().getColor(R.color.purple));
            }
        }.start();
    }
}

