package com.example.tradetea;

public class RecyclerViewModel {
    //Create the variables we want to operate
    String id,title,desc,imageuri;

    //empty constructor needed
    public RecyclerViewModel(){}

    public RecyclerViewModel(String id,String title,String desc,String mImageUri){
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.imageuri = mImageUri;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageuri() { return imageuri; }

    public void setImageuri(String imageuri) { this.imageuri = imageuri; }
}
