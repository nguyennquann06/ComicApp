package com.example.bt_lon_ck;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    private TextView welcomeTextView, dateTimeTextView;
    private Button guestButton, adminButton;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        welcomeTextView = findViewById(R.id.welcomeTextView);
        dateTimeTextView = findViewById(R.id.dateTimeTextView);
        guestButton = findViewById(R.id.guestButton);
        adminButton = findViewById(R.id.adminButton);

        // Cập nhật ngày giờ
        handler = new Handler(Looper.getMainLooper());
        updateDateTime();
        handler.postDelayed(this::updateDateTime, 1000);

        // Sự kiện click
        guestButton.setOnClickListener(v -> startActivity(new Intent(StartActivity.this, MainActivity.class)));
        adminButton.setOnClickListener(v -> startActivity(new Intent(StartActivity.this, AdminLoginActivity.class)));
    }

    private void updateDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(new Date());
        dateTimeTextView.setText(currentDateTime);
        handler.postDelayed(this::updateDateTime, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}