package com.example.bt_lon_ck;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ComicAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button addButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);
        logoutButton = findViewById(R.id.logoutButton);
        dbHelper = new DatabaseHelper(this);

        loadComics();

        addButton.setOnClickListener(v -> showAddComicDialog());

        logoutButton.setOnClickListener(v -> {
            // Chuyển về màn hình đăng nhập AdminLoginActivity
            Intent intent = new Intent(AdminDashboardActivity.this, AdminLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack Activity
            startActivity(intent);
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            finish(); // Đóng AdminDashboardActivity
        });
    }

    private void loadComics() {
        List<Comic> comicList = dbHelper.getAllComics();
        adapter = new ComicAdapter(comicList, this, true);
        adapter.setOnComicEditListener(this::showEditComicDialog);
        adapter.setOnComicDeleteListener(this::showDeleteComicDialog);
        adapter.setOnComicClickListener(comic -> {
            Intent intent = new Intent(this, AdminAdapterActivity.class);
            intent.putExtra("comic_id", comic.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showAddComicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_comic, null);
        builder.setView(dialogView);

        EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        EditText authorEditText = dialogView.findViewById(R.id.authorEditText);
        EditText descEditText = dialogView.findViewById(R.id.descEditText);
        EditText imageEditText = dialogView.findViewById(R.id.imageEditText);

        builder.setTitle("Thêm truyện mới")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String title = titleEditText.getText().toString();
                    String author = authorEditText.getText().toString();
                    String description = descEditText.getText().toString();
                    String image = imageEditText.getText().toString();

                    if (!title.isEmpty() && !author.isEmpty()) {
                        Comic comic = new Comic(0, title, author, description, image);
                        long result = dbHelper.addComic(comic);
                        if (result == -1) {
                            Toast.makeText(this, "Truyện đã tồn tại!", Toast.LENGTH_SHORT).show();
                        } else {
                            loadComics();
                            Toast.makeText(this, "Đã thêm truyện", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void showEditComicDialog(Comic comic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_comic, null);
        builder.setView(dialogView);

        EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        EditText authorEditText = dialogView.findViewById(R.id.authorEditText);
        EditText descEditText = dialogView.findViewById(R.id.descEditText);
        EditText imageEditText = dialogView.findViewById(R.id.imageEditText);

        titleEditText.setText(comic.getTitle());
        authorEditText.setText(comic.getAuthor());
        descEditText.setText(comic.getDescription());
        imageEditText.setText(comic.getImage());

        builder.setTitle("Sửa truyện")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String title = titleEditText.getText().toString();
                    String author = authorEditText.getText().toString();
                    String description = descEditText.getText().toString();
                    String image = imageEditText.getText().toString();

                    if (!title.isEmpty() && !author.isEmpty()) {
                        comic.setTitle(title);
                        comic.setAuthor(author);
                        comic.setDescription(description);
                        comic.setImage(image);
                        dbHelper.updateComic(comic);
                        loadComics();
                        Toast.makeText(this, "Đã cập nhật truyện", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void showDeleteComicDialog(Comic comic) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa truyện")
                .setMessage("Bạn có chắc muốn xóa " + comic.getTitle() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteComic(comic.getId());
                    loadComics();
                    Toast.makeText(this, "Đã xóa truyện", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}