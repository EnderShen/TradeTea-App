package com.example.tradetea;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookRecyclerViewFragment extends Fragment {

    private RecyclerView bookR;
    private FirebaseFirestore db;
    private MyAdapter adapter;
    private List<RecyclerViewModel> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_book_recycler_view, container, false);
        bookR = v.findViewById(R.id.BookRecyclerView);
        bookR.setHasFixedSize(true);
        bookR.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapter = new MyAdapter(getActivity(),list);
        bookR.setAdapter(adapter);

        ShowData();
        return v;
    }

    private void ShowData(){
        db.collection("book").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                for(DocumentSnapshot snapshot : task.getResult()){
                    RecyclerViewModel model = new RecyclerViewModel(snapshot.getString("id"), snapshot.getString("title"), snapshot.getString("desc"),snapshot.getString("image"));
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Toast.makeText(getActivity(), "recieve success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "recieve Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}