package com.example.iot;

public class ImageData {
    private final int imageResId;
    private final String description;

    public ImageData(int imageResId, String description) {
        this.imageResId = imageResId;
        this.description = description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDescription() {
        return description;
    }
}
