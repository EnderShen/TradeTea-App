package com.example.tradetea;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.UUID;

import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;



public class Upload extends Fragment {

    private EditText mtitle,mdescribtion ;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button publishBT;
    private ImageView image;
    private FirebaseFirestore db;

    private Uri imageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        mtitle = view.findViewById(R.id.WriteTitle);
        mdescribtion = view.findViewById(R.id.Edit_Describtion);
        publishBT = view.findViewById(R.id.button_upload);
        image = view.findViewById(R.id.Upload_image);

        db = FirebaseFirestore.getInstance();
        image.setImageURI(imageUri);


        uploadFile();
        ChooseImage();
        Publish();

        return view;
    }

    public void ChooseImage(){

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }else{
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i,RESULT_LOAD_IMAGE);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMAGE){
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    public void Publish(){
        publishBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mtitle.getText().toString();
                String desc = mdescribtion.getText().toString();
                String uri = imageUri.toString();
                String id = UUID.randomUUID().toString();

                saveToFireStore(id,title,desc,uri);
                mtitle.setText("");
                mdescribtion.setText("");
            }
        });
    }

    public void saveToFireStore(String id, String title, String desc,String imageUri)
    {
        if(!title.isEmpty() && !desc.isEmpty())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",id);
            map.put("title",title);
            map.put("desc",desc);
            map.put("image",imageUri);

            db.collection("data").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(),"Succsee",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(getActivity(),"Please Enter all information", Toast.LENGTH_SHORT).show();
        }
    }

    private  void uploadFile(){

        if(imageUri != null){
            StorageReference fileRef = mStorageRef.child(System.currentTimeMillis()+"."+"jpg");

            //if upload successful run this
            mUploadTask=fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            // get the image uri and pass the data from database.
                            final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUri = uri.toString();
                                    UploadModel UPLOAD = new UploadModel(downloadUri);
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(UPLOAD);
                                }
                            });

                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            //set value to progress bar
                            double progress = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        }
                    });
        }else {
            //if image is not selected show this toast
            Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}