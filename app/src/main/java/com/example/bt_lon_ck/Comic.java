package com.example.bt_lon_ck;

public class Comic {
    private int id;
    private String title;
    private String author;
    private String description;
    private String image; // Thêm thuộc tính image

    public Comic() {}

    public Comic(int id, String title, String author, String description, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.image = image;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}