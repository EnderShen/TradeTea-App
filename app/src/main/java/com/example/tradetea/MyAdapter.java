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
    private OnItemListener monItemListener;

    public MyAdapter(Context context, List<RecyclerViewModel> list, OnItemListener onItemListener) {
        mcontext = context;
        mList = list;

        this.monItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.recyclerviewitems, parent, false);
        return new MyViewHolder(v, monItemListener);
    }

    public interface OnItemListener {
        void onItemClick(int position);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titel, desc;
        ImageView image;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            titel = itemView.findViewById(R.id.rTitle);
            desc = itemView.findViewById(R.id.rDescribtion);
            image = itemView.findViewById(R.id.rIamge);

            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
}
