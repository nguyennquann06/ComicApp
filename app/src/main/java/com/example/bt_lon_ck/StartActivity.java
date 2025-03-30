package com.example.bt_lon_ck;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Ẩn Action Bar nếu cần
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button guestButton = findViewById(R.id.guestButton);
        Button adminButton = findViewById(R.id.adminButton);

        // Nút Guest: Chuyển đến MainActivity
        guestButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Nút Admin: Chuyển đến AdminLoginActivity
        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });
    }
}