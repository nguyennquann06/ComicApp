package com.example.bt_lon_ck;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View; // Thêm import này
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ComicAdapter adapter;
    private DatabaseHelper dbHelper;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        dbHelper = new DatabaseHelper(this);

        loadComics();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchComics(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadComics() {
        List<Comic> comicList = dbHelper.getAllComics();
        Log.d(TAG, "Loaded " + comicList.size() + " comics");
        if (comicList.isEmpty()) {
            Log.e(TAG, "No comics found in database");
        } else {
            for (Comic comic : comicList) {
                Log.d(TAG, "Comic: " + comic.getTitle() + " by " + comic.getAuthor());
            }
        }
        adapter = new ComicAdapter(comicList, this, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.VISIBLE); // Đảm bảo RecyclerView hiển thị
    }

    private void searchComics(String keyword) {
        List<Comic> comicList = dbHelper.searchComics(keyword);
        Log.d(TAG, "Search result: " + comicList.size() + " comics for keyword: " + keyword);
        adapter.updateList(comicList);
    }
}