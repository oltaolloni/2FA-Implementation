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
    private MailSender mailSender = new MailSender();
    private EditText otpET;
    private TextView resendBtn;
    private boolean resendEnable = false;
    private int resendTime = 60;
    private String otpSent;  // For demo purposes only; retrieve this securely in production.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify);

        otpET = findViewById(R.id.otpET);
        resendBtn = findViewById(R.id.resendBtn);
        final TextView otpEmail = findViewById(R.id.otpEmail);
        final Button verifyBtn = findViewById(R.id.verifyBtn);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final String email = getIntent().getStringExtra("email");
        otpEmail.setText(email);

        // Generate and send OTP for the first time when entering this screen
        String generatedOtp = getIntent().getStringExtra("otp");

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = otpET.getText().toString();

                if (enteredOtp.equals(generatedOtp)) {
                    Toast.makeText(VerifyActivity.this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();
                    // Proceed to next activity or grant access
                    startActivity(new Intent(VerifyActivity.this, SuccessActivity.class));
                } else {
                    Toast.makeText(VerifyActivity.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startCountDownTimer();

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendEnable) {
                    String newOtp = generateOtp();
                    sendOtpEmail(email, newOtp);
                    startCountDownTimer();
                }
            }
        });
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    private void sendOtpEmail(String email, String otp) {
        new Thread(() -> {
            try {
                mailSender.sendOtpEmail(email, otp);
                runOnUiThread(() -> Toast.makeText(VerifyActivity.this, "OTP sent to " + email, Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                runOnUiThread(() -> Toast.makeText(VerifyActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
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

