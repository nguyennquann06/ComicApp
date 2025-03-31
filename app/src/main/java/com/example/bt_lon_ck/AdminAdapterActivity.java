package com.example.bt_lon_ck;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminAdapterActivity extends AppCompatActivity {
    private static final String TAG = "AdminAdapterActivity";
    private RecyclerView recyclerView;
    private ChapterAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button addChapterButton;
    private int comicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chapter);

        Log.d(TAG, "AdminAdapterActivity started");

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerView);
        addChapterButton = findViewById(R.id.addChapterButton);
        dbHelper = new DatabaseHelper(this);
        comicId = getIntent().getIntExtra("comic_id", -1);

        if (comicId == -1) {
            Log.e(TAG, "Invalid comic_id received");
            Toast.makeText(this, "Không tìm thấy truyện", Toast.LENGTH_SHORT).show();
            finish(); // Thoát nếu comic_id không hợp lệ
            return;
        }

        Log.d(TAG, "Comic ID: " + comicId);

        // Kiểm tra xem các view có được khởi tạo không
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView is null");
        } else {
            Log.d(TAG, "RecyclerView initialized");
        }
        if (addChapterButton == null) {
            Log.e(TAG, "AddChapterButton is null");
        } else {
            Log.d(TAG, "AddChapterButton initialized");
        }

        loadChapters();

        addChapterButton.setOnClickListener(v -> {
            Log.d(TAG, "Add Chapter button clicked");
            showAddChapterDialog();
        });
    }

    private void loadChapters() {
        List<Chapter> chapterList = dbHelper.getChaptersByComicId(comicId);
        Log.d(TAG, "Loaded " + chapterList.size() + " chapters for comic ID: " + comicId);
        if (chapterList.isEmpty()) {
            Log.w(TAG, "No chapters found for this comic");
            Toast.makeText(this, "Chưa có chapter nào", Toast.LENGTH_SHORT).show();
        } else {
            for (Chapter chapter : chapterList) {
                Log.d(TAG, "Chapter: " + chapter.getChapterTitle() + " (Image: " + chapter.getChapterImage() + ")");
            }
        }
        adapter = new ChapterAdapter(chapterList, this);
        adapter.setOnChapterEditListener(this::showEditChapterDialog);
        adapter.setOnChapterDeleteListener(this::showDeleteChapterDialog);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showAddChapterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_chapter, null);
        builder.setView(dialogView);

        EditText chapterNumberEditText = dialogView.findViewById(R.id.chapterNumberEditText);
        EditText chapterTitleEditText = dialogView.findViewById(R.id.chapterTitleEditText);
        EditText chapterImageEditText = dialogView.findViewById(R.id.chapterImageEditText);

        builder.setTitle("Thêm chapter mới")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String chapterNumberStr = chapterNumberEditText.getText().toString();
                    String chapterTitle = chapterTitleEditText.getText().toString();
                    String chapterImage = chapterImageEditText.getText().toString();

                    if (!chapterNumberStr.isEmpty() && !chapterTitle.isEmpty() && !chapterImage.isEmpty()) {
                        int chapterNumber = Integer.parseInt(chapterNumberStr);
                        Chapter chapter = new Chapter();
                        chapter.setComicId(comicId);
                        chapter.setChapterNumber(chapterNumber);
                        chapter.setChapterTitle(chapterTitle);
                        chapter.setChapterImage(chapterImage);
                        dbHelper.addChapter(chapter);
                        Log.d(TAG, "Added chapter: " + chapterTitle + " (Image: " + chapterImage + ")");
                        loadChapters();
                        Toast.makeText(this, "Đã thêm chapter", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void showEditChapterDialog(Chapter chapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_chapter, null);
        builder.setView(dialogView);

        EditText chapterNumberEditText = dialogView.findViewById(R.id.chapterNumberEditText);
        EditText chapterTitleEditText = dialogView.findViewById(R.id.chapterTitleEditText);
        EditText chapterImageEditText = dialogView.findViewById(R.id.chapterImageEditText);

        chapterNumberEditText.setText(String.valueOf(chapter.getChapterNumber()));
        chapterTitleEditText.setText(chapter.getChapterTitle());
        chapterImageEditText.setText(chapter.getChapterImage());

        builder.setTitle("Sửa chapter")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String chapterNumberStr = chapterNumberEditText.getText().toString();
                    String chapterTitle = chapterTitleEditText.getText().toString();
                    String chapterImage = chapterImageEditText.getText().toString();

                    if (!chapterNumberStr.isEmpty() && !chapterTitle.isEmpty() && !chapterImage.isEmpty()) {
                        int chapterNumber = Integer.parseInt(chapterNumberStr);
                        chapter.setChapterNumber(chapterNumber);
                        chapter.setChapterTitle(chapterTitle);
                        chapter.setChapterImage(chapterImage);
                        dbHelper.updateChapter(chapter);
                        Log.d(TAG, "Updated chapter: " + chapterTitle + " (Image: " + chapterImage + ")");
                        loadChapters();
                        Toast.makeText(this, "Đã cập nhật chapter", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void showDeleteChapterDialog(Chapter chapter) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa chapter")
                .setMessage("Bạn có chắc muốn xóa " + chapter.getChapterTitle() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteChapter(chapter.getChapterId());
                    Log.d(TAG, "Deleted chapter: " + chapter.getChapterTitle());
                    loadChapters();
                    Toast.makeText(this, "Đã xóa chapter", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}