package com.example.tradetea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context mcontext;
    private List<RecyclerViewModel> mList;

    public MyAdapter(Context context, List<RecyclerViewModel> list)
    {
        mcontext = context;
        mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mcontext).inflate(R.layout.recyclerviewitems,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RecyclerViewModel recyclerViewModel = mList.get(position);
        holder.titel.setText(mList.get(position).getTitle());
        holder.desc.setText(mList.get(position).getDesc());
        String imageuri = recyclerViewModel.getImageuri();
        Picasso.get().load(imageuri).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titel,desc;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titel = itemView.findViewById(R.id.rTitle);
            desc = itemView.findViewById(R.id.rDescribtion);
            image = itemView.findViewById(R.id.rIamge);

        }
    }
}
