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
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


public class Upload extends Fragment {

    private EditText mtitle, mdescribtion;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button publishBT;
    private ImageView image;
    private FirebaseFirestore db;

    private Uri imageUri;
    private String URL;
    private Spinner mUserSelectSpinner;
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
        mUserSelectSpinner = view.findViewById(R.id.UserSelectSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.UploadSelection, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mUserSelectSpinner.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        image.setImageURI(imageUri);


        ChooseImage();
        Publish();

        return view;
    }

    public void ChooseImage() {

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    public void Publish() {
        publishBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mtitle.getText().toString();
                String desc = mdescribtion.getText().toString();
                String id = UUID.randomUUID().toString();
                uploadFile(id, title, desc);
                mtitle.setText("");
                mdescribtion.setText("");
            }
        });
    }

    private void uploadFile(String id, String title, String desc) {

        if (imageUri != null) {
            StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + "jpg");
            mUploadTask = fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            // get the image uri and pass the url from database.
                            final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();

                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    URL = uri.toString();
                                    String message = mUserSelectSpinner.getSelectedItem().toString();

                                    if (!title.isEmpty() && !desc.isEmpty()) {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("id", id);
                                        map.put("title", title);
                                        map.put("desc", desc);
                                        map.put("image", URL);

                                        if (message.contains("Book")) {
                                            db.collection("book").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Succsee", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        if (message.contains("Devices")) {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                            db.collection("other").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Succsee", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Please Enter all information", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            //if image is not selected show this toast
            Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}