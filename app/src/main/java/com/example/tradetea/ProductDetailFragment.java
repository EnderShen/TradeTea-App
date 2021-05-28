package com.example.tradetea;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetailFragment extends Fragment {

    TextView description, title, Phone;
    ImageView ItemImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_detail, container, false);

        description = v.findViewById(R.id.dDetail);
        title = v.findViewById(R.id.dTitle);
        Phone = v.findViewById(R.id.dPhone);
        ItemImage = v.findViewById(R.id.dImageView);


        String imageURL;
        title.setText(getArguments().getString("title"));
        description.setText(getArguments().getString("Des"));
        Phone.setText(getArguments().getString("phone"));
        imageURL = getArguments().getString("ImageURL");

        //use picasso to load the image and display it
        Picasso.get().load(imageURL).into(ItemImage);
        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}