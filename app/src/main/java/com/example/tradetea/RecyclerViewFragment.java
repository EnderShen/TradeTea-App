package com.example.tradetea;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewFragment extends Fragment implements MyAdapter.OnItemListener {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private MyAdapter adapter;
    private FirebaseRecyclerAdapter<RecyclerViewModel, MyAdapter.MyViewHolder> mFireBaseRecyclerAdapter;
    private List<RecyclerViewModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.DisplayRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        adapter = new MyAdapter(getActivity(), list, this);
        recyclerView.setAdapter(adapter);

        ShowData();
        return view;
    }

    private void ShowData() {
        db.collection("IT").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                for (DocumentSnapshot snapshot : task.getResult()) {
                    RecyclerViewModel model = new RecyclerViewModel(snapshot.getString("id"),
                            snapshot.getString("title"),
                            snapshot.getString("desc"),
                            snapshot.getString("image"),
                            snapshot.getString("phone"));
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "Load success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "Load Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {

        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        Bundle arges = new Bundle();

        arges.putString("title", list.get(position).getTitle());
        arges.putString("Des", list.get(position).getDesc());
        arges.putString("ImageURL", list.get(position).getImageuri());
        arges.putString("phone", list.get(position).getContactnumber());

        productDetailFragment.setArguments(arges);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, productDetailFragment).addToBackStack(null).commit();
    }
}