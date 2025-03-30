package com.example.bt_lon_ck;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ComicAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);
        dbHelper = new DatabaseHelper(this);

        loadComics();

        addButton.setOnClickListener(v -> {
            // Thêm dialog để nhập thông tin truyện mới
        });
    }

    private void loadComics() {
        List<Comic> comicList = dbHelper.getAllComics();
        adapter = new ComicAdapter(comicList, this, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}