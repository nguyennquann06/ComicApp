package com.example.bt_lon_ck;

import android.app.AlertDialog;
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

public class AdminChapterActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChapterAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button addChapterButton;
    private int comicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerView);
        addChapterButton = findViewById(R.id.addChapterButton);
        dbHelper = new DatabaseHelper(this);
        comicId = getIntent().getIntExtra("comic_id", -1);

        loadChapters();

        addChapterButton.setOnClickListener(v -> showAddChapterDialog());
    }

    private void loadChapters() {
        List<Chapter> chapterList = dbHelper.getChaptersByComicId(comicId);
        adapter = new ChapterAdapter(chapterList, this);
        adapter.setOnChapterEditListener(this::showEditChapterDialog);
        adapter.setOnChapterDeleteListener(this::showDeleteChapterDialog);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

                    if (!chapterNumberStr.isEmpty() && !chapterTitle.isEmpty()) {
                        int chapterNumber = Integer.parseInt(chapterNumberStr);
                        Chapter chapter = new Chapter();
                        chapter.setComicId(comicId);
                        chapter.setChapterNumber(chapterNumber);
                        chapter.setChapterTitle(chapterTitle);
                        chapter.setChapterImage(chapterImage);
                        dbHelper.addChapter(chapter);
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

                    if (!chapterNumberStr.isEmpty() && !chapterTitle.isEmpty()) {
                        int chapterNumber = Integer.parseInt(chapterNumberStr);
                        chapter.setChapterNumber(chapterNumber);
                        chapter.setChapterTitle(chapterTitle);
                        chapter.setChapterImage(chapterImage);
                        dbHelper.updateChapter(chapter);
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
                    loadChapters();
                    Toast.makeText(this, "Đã xóa chapter", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}