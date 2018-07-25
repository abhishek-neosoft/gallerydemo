package com.example.webworks.galleryapp.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.webworks.galleryapp.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.RecyclerViewHolder> {

    private Context context;
    private ArrayList images;

    HomeAdapter(Context context, ArrayList images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.imagepath,viewGroup,false);
        RecyclerViewHolder holder=new RecyclerViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {

        Glide.with(context).load(images.get(i)).into(recyclerViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.set_image);

        }
    }
}
