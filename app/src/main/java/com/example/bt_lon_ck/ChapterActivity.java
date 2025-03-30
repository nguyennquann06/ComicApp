package com.example.bt_lon_ck;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ChapterActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private List<Chapter> chapterList;
    private int currentChapterIndex = 0;
    private ImageView chapterImageView;
    private TextView chapterTitleTextView;
    private Button prevButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        chapterImageView = findViewById(R.id.chapterImageView);
        chapterTitleTextView = findViewById(R.id.chapterTitleTextView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        dbHelper = new DatabaseHelper(this);
        int comicId = getIntent().getIntExtra("comic_id", -1);
        chapterList = dbHelper.getChaptersByComicId(comicId);

        if (!chapterList.isEmpty()) {
            displayChapter(currentChapterIndex);
        }

        prevButton.setOnClickListener(v -> {
            if (currentChapterIndex > 0) {
                currentChapterIndex--;
                displayChapter(currentChapterIndex);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (currentChapterIndex < chapterList.size() - 1) {
                currentChapterIndex++;
                displayChapter(currentChapterIndex);
            }
        });
    }

    private void displayChapter(int index) {
        Chapter chapter = chapterList.get(index);
        chapterTitleTextView.setText(chapter.getChapterTitle());

        int resId = getResources().getIdentifier(chapter.getChapterImage(), "drawable", getPackageName());
        if (resId != 0) {
            chapterImageView.setImageResource(resId);
        } else {
            chapterImageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Enable/disable nút dựa trên vị trí chap
        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < chapterList.size() - 1);
    }
}