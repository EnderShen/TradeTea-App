package com.example.tradetea;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class RecyclerViewFragment extends Fragment implements MyAdapter.OnItemListener {

    private RecyclerView recyclerView;
    private Button SearchBT;
    private EditText Keyword;
    private Spinner mSelectSpinner;
    private FirebaseFirestore db;
    private MyAdapter adapter;
    private List<RecyclerViewModel> list;
    private List<RecyclerViewModel> FilterResultList;
    private MyAdapter FilterResultAdapter;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.DisplayRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mediaPlayer = MediaPlayer.create(getActivity(),R.raw.buttonsound);

        mSelectSpinner = view.findViewById(R.id.SelectCategory);
        SearchBT = view.findViewById(R.id.SearchBT);
        Keyword = view.findViewById(R.id.SearchItems);

        ArrayAdapter<CharSequence> Spinneradapter = ArrayAdapter.createFromResource(getActivity(), R.array.Selections, R.layout.support_simple_spinner_dropdown_item);
        Spinneradapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSelectSpinner.setAdapter(Spinneradapter);

        list = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), list, this);
        recyclerView.setAdapter(adapter);

        FilterResultList = new ArrayList<>();
        FilterResultAdapter = new MyAdapter(getActivity(), FilterResultList, this);

        ChangeCategory();
        SearchProducts();
        return view;
    }

    //Search specific items from recycler view by entering keywords
    private void SearchProducts() {

        SearchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.start();
                if (Keyword.getText().length() != 0) {
                    String keyword = Keyword.getText().toString();

                    for (int i = 0; i < list.size(); i++) {

                        if (list.get(i).getTitle().contains(keyword)) {
                            FilterResultList.add(list.get(i));
                        }
                    }

                    FilterResultAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(FilterResultAdapter);

                } else {
                    Toast.makeText(getActivity(), "Please, Enter your keyword", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // When this function called, store data into recycler view
    private void ShowData(String path) {
        db.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    // When spinner item selected, reload the recycler view
    public void ChangeCategory() {
        mSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.clear();
                adapter.notifyDataSetChanged();
                String category = mSelectSpinner.getSelectedItem().toString();
                Toast.makeText(getContext(), category, Toast.LENGTH_SHORT).show();
                ShowData(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //If recycler view item clicked, using bundle to pass the information to another fragment
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

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}