package com.example.tradetea;

public class RecyclerViewModel {
    //Create the variables we want to operate
    String id,title,desc,imageuri,Contactnumber;

    //empty constructor
    public RecyclerViewModel(){}

    public RecyclerViewModel(String id,String title,String desc,String mImageUri){
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.imageuri = mImageUri;
    }

    public RecyclerViewModel(String id,String title,String desc,String mImageUri,String contactNumber){
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.imageuri = mImageUri;
        this.Contactnumber = contactNumber;
    }

    public String getContactnumber() {
        return Contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        Contactnumber = contactnumber;
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
