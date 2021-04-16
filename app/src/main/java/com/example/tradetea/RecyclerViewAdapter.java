package com.example.tradetea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<RecyclerViewModel> mList;

    public RecyclerViewAdapter(Context context, List<RecyclerViewModel> mList)
    {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerviewitems,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RecyclerViewModel recyclerViewModel = mList.get(position);
        holder.desc.setText(mList.get(position).getdesc().toString());
        holder.title.setText(mList.get(position).getTitle().toString());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,desc,id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.rid);
            title = itemView.findViewById(R.id.WriteTitle);
            desc = itemView.findViewById(R.id.Edit_Describtion);
        }
    }
}
