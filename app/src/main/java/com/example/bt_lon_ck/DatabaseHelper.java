package com.example.bt_lon_ck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ComicDB";
    private static final int DATABASE_VERSION = 3; // Tăng version để thêm bảng chapters
    private static final String TAG = "DatabaseHelper";

    // Comics table
    private static final String TABLE_COMICS = "comics";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_IMAGE = "image";

    // Chapters table
    private static final String TABLE_CHAPTERS = "chapters";
    private static final String COLUMN_CHAPTER_ID = "chapter_id";
    private static final String COLUMN_COMIC_ID = "comic_id"; // Foreign key liên kết với comics
    private static final String COLUMN_CHAPTER_NUMBER = "chapter_number";
    private static final String COLUMN_CHAPTER_TITLE = "chapter_title";
    private static final String COLUMN_CHAPTER_IMAGE = "chapter_image";

    // Admins table
    private static final String TABLE_ADMIN = "admins";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng comics
        String createComicTable = "CREATE TABLE " + TABLE_COMICS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_AUTHOR + " TEXT,"
                + COLUMN_DESC + " TEXT,"
                + COLUMN_IMAGE + " TEXT)";
        db.execSQL(createComicTable);

        // Tạo bảng chapters
        String createChapterTable = "CREATE TABLE " + TABLE_CHAPTERS + "("
                + COLUMN_CHAPTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COMIC_ID + " INTEGER,"
                + COLUMN_CHAPTER_NUMBER + " INTEGER,"
                + COLUMN_CHAPTER_TITLE + " TEXT,"
                + COLUMN_CHAPTER_IMAGE + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_COMIC_ID + ") REFERENCES " + TABLE_COMICS + "(" + COLUMN_ID + "))";
        db.execSQL(createChapterTable);

        // Tạo bảng admin
        String createAdminTable = "CREATE TABLE " + TABLE_ADMIN + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY,"
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createAdminTable);

        // Thêm admin mặc định
        db.execSQL("INSERT INTO " + TABLE_ADMIN + " VALUES ('admin', 'password123')");

        // Thêm dữ liệu mẫu cho comics
        db.execSQL("INSERT INTO " + TABLE_COMICS + " VALUES (null, 'One Piece', 'Eiichiro Oda', 'Câu chuyện về Luffy', 'one_piece')");
        db.execSQL("INSERT INTO " + TABLE_COMICS + " VALUES (null, 'Naruto', 'Masashi Kishimoto', 'Hành trình của Naruto', 'naruto')");
        db.execSQL("INSERT INTO " + TABLE_COMICS + " VALUES (null, 'One Punch Man', 'ONE', 'Siêu anh hùng Saitama', 'one_punch_man')");
        db.execSQL("INSERT INTO " + TABLE_COMICS + " VALUES (null, 'Dragon Ball', 'Akira Toriyama', 'Cuộc phiêu lưu của Goku', 'dragon_ball')");
        db.execSQL("INSERT INTO " + TABLE_COMICS + " VALUES (null, 'Cô Bé Quàng Khăn Đỏ', 'Brothers Grimm', 'Câu chuyện cổ tích', 'little_red_riding_hood')");

        // Thêm chapters cho "Cô Bé Quàng Khăn Đỏ" (comic_id = 5)
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 1, 'Chap 1', 'chapter_1_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 2, 'Chap 2', 'chapter_2_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 3, 'Chap 3', 'chapter_3_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 4, 'Chap 3', 'chapter_4_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 5, 'Chap 3', 'chapter_5_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 6, 'Chap 3', 'chapter_6_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 7, 'Chap 3', 'chapter_7_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 8, 'Chap 3', 'chapter_8_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 9, 'Chap 3', 'chapter_9_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 10, 'Chap 3', 'chapter_10_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 11, 'Chap 3', 'chapter_11_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 12, 'Chap 3', 'chapter_12_kd')");
        db.execSQL("INSERT INTO " + TABLE_CHAPTERS + " VALUES (null, 5, 13, 'Chap 3', 'chapter_13_kd')");

        Log.d(TAG, "Database created and sample data inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAPTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        onCreate(db);
    }

    // Lấy tất cả chapters của một truyện
    public List<Chapter> getChaptersByComicId(int comicId) {
        List<Chapter> chapterList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHAPTERS + " WHERE " + COLUMN_COMIC_ID + "=?",
                new String[]{String.valueOf(comicId)});
        if (cursor.moveToFirst()) {
            do {
                Chapter chapter = new Chapter();
                chapter.setChapterId(cursor.getInt(0));
                chapter.setComicId(cursor.getInt(1));
                chapter.setChapterNumber(cursor.getInt(2));
                chapter.setChapterTitle(cursor.getString(3));
                chapter.setChapterImage(cursor.getString(4));
                chapterList.add(chapter);
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "Fetched " + chapterList.size() + " chapters for comic ID: " + comicId);
        cursor.close();
        db.close();
        return chapterList;
    }

    // Các phương thức khác giữ nguyên
    public void addComic(Comic comic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, comic.getTitle());
        values.put(COLUMN_AUTHOR, comic.getAuthor());
        values.put(COLUMN_DESC, comic.getDescription());
        values.put(COLUMN_IMAGE, comic.getImage());
        long id = db.insert(TABLE_COMICS, null, values);
        Log.d(TAG, "Added comic with ID: " + id);
        db.close();
    }

    public void updateComic(Comic comic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, comic.getTitle());
        values.put(COLUMN_AUTHOR, comic.getAuthor());
        values.put(COLUMN_DESC, comic.getDescription());
        values.put(COLUMN_IMAGE, comic.getImage());
        int rows = db.update(TABLE_COMICS, values, COLUMN_ID + "=?", new String[]{String.valueOf(comic.getId())});
        Log.d(TAG, "Updated " + rows + " rows for comic ID: " + comic.getId());
        db.close();
    }

    public void deleteComic(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_COMICS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        Log.d(TAG, "Deleted " + rows + " rows for comic ID: " + id);
        db.close();
    }

    public List<Comic> getAllComics() {
        List<Comic> comicList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMICS, null);
        if (cursor.moveToFirst()) {
            do {
                Comic comic = new Comic();
                comic.setId(cursor.getInt(0));
                comic.setTitle(cursor.getString(1));
                comic.setAuthor(cursor.getString(2));
                comic.setDescription(cursor.getString(3));
                comic.setImage(cursor.getString(4));
                comicList.add(comic);
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "Fetched " + comicList.size() + " comics from database");
        cursor.close();
        db.close();
        return comicList;
    }

    public List<Comic> searchComics(String keyword) {
        List<Comic> comicList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMICS + " WHERE " +
                        COLUMN_TITLE + " LIKE ? OR " + COLUMN_AUTHOR + " LIKE ?",
                new String[]{"%" + keyword + "%", "%" + keyword + "%"});
        if (cursor.moveToFirst()) {
            do {
                Comic comic = new Comic();
                comic.setId(cursor.getInt(0));
                comic.setTitle(cursor.getString(1));
                comic.setAuthor(cursor.getString(2));
                comic.setDescription(cursor.getString(3));
                comic.setImage(cursor.getString(4));
                comicList.add(comic);
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "Search found " + comicList.size() + " comics for keyword: " + keyword);
        cursor.close();
        db.close();
        return comicList;
    }

    public boolean checkAdmin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADMIN + " WHERE " +
                        COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        Log.d(TAG, "Admin check for " + username + ": " + (exists ? "Success" : "Failed"));
        cursor.close();
        db.close();
        return exists;
    }
}