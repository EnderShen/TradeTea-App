package com.example.tradetea;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.UUID;


public class Upload extends Fragment {

    private EditText mtitle,mdescribtion ;
    private Button publishBT;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        mtitle = view.findViewById(R.id.WriteTitle);
        mdescribtion = view.findViewById(R.id.Edit_Describtion);
        publishBT = view.findViewById(R.id.button_upload);

        db = FirebaseFirestore.getInstance();

        publishBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mtitle.getText().toString();
                String desc = mdescribtion.getText().toString();
                String id = UUID.randomUUID().toString();

                saveToFireStore(id,title,desc);
            }
        });
        return view;
    }

    public void saveToFireStore(String id, String title, String desc)
    {
        if(!title.isEmpty() && !desc.isEmpty())
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",id);
            map.put("title",title);
            map.put("desc",desc);

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
}