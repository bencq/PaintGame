package com.example.deep.paintgame.javaBean;

/**
 * Created by Bencq on 2018/04/15.
 */

public class Problem {
    private String name;
    private int size;
    private String data;
    private int imageId;

    public Problem(String name, int size, String data, int imageId) {
        this.name = name;
        this.size = size;
        this.data = data;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String getData() {
        return data;
    }

    public int getImageId() {
        return imageId;
    }
}
