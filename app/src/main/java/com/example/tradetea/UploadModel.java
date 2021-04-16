package com.example.tradetea;

public class UploadModel {
    //Create fields we want to deal with
    private String mTitle;
    private String mID;
    private String mdesc;

    //empty constructor needed
    public UploadModel(){

    }

    //a normal constructor
    public UploadModel(String title, String Id,String desc) {

        if(title.trim().equals("")){
            title = "no Title";
        }

        if(desc.trim().equals("")){
            desc= "no description";
        }

        this.mTitle = title;
        this.mID = Id;
        this.mdesc = desc;
    }

    //create get and set return type method for those fields
    public String getmTitle() { return mTitle; }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmID() {
        return mID;
    }

    public void getmID(String Id) {
        this.mID = Id;
    }

    public String getdesc() { return mdesc; }

    public void setdesc (String desc) { this.mdesc = desc; }
}
