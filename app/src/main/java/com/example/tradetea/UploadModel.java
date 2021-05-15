package com.example.tradetea;

public class UploadModel {
    //Create fields we want to deal with

    private String mImageUri;

    //empty constructor neeeded
    public UploadModel(){

    }

    //a normal constructor
    public UploadModel(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}
