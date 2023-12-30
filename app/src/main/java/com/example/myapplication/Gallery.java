package com.example.myapplication;

import android.graphics.Bitmap;

public class Gallery {
    private int Photo = -1;
    private Bitmap bitMap;

    public Gallery() {
    }

    public Gallery(int photo) {
        Photo = photo;
    }

    public Gallery(Bitmap bitmap) { bitMap = bitmap; }

    public int getPhoto() {
        return Photo;
    }

    public void setPhoto(int photo) {
        Photo = photo;
    }

    public int getPhotoBack(){

        return Photo - 1;
    }

    public void setPhotoBack(int photoBack){
        Photo = photoBack;
    }

    public int getPhotoNext(){
        return Photo + 1;
    }

    public void setPhotoNext(int photoNext){
        Photo = photoNext;
    }

    public Bitmap getBitmap() { return bitMap; }

    public void setBitmap(Bitmap bitmap) { bitMap = bitmap;}
}