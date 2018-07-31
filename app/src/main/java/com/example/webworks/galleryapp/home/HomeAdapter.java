package com.example.webworks.galleryapp.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.webworks.galleryapp.R;
import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.RecyclerViewHolder> {

    private Context context;
    private ArrayList images;
    private boolean longClickTouch=false;
    private ArrayList items;
    private SparseBooleanArray selectedItems;
    OnRecyclerViewItemPosition itemPosition;


    HomeAdapter(Context context, ArrayList images) {
        this.context = context;
        this.images = images;
        selectedItems=new SparseBooleanArray();
        this.itemPosition = (OnRecyclerViewItemPosition) context;
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
        if (isSelected(i))
        {
            recyclerViewHolder.imageView.setBackgroundResource(R.drawable.border_for_images);
        }
        else
        {
            recyclerViewHolder.imageView.setBackgroundResource(0);
        }
        if (items.size()==0)
        {
            longClickTouch=false;
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        RecyclerViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.set_image);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longClickTouch)
                    {
                        return false;
                    }
                    else
                    {
                        toggleSelection(getAdapterPosition());
                        longClickTouch=true;
                    }
                    return true;
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (longClickTouch)
                    {
                        toggleSelection(getAdapterPosition());
                    }
                }
            });

        }
    }
    private void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    private boolean isSelected(int position){
        return getSelectedItems().contains(position);
    }

    private ArrayList getSelectedItems(){
        items = new ArrayList(selectedItems.size());
        for (int i=0;i<selectedItems.size();i++)
        {
           items.add(selectedItems.keyAt(i));
        }


        itemPosition.selectedItemPosition(items);
        return items;
    }
    public interface OnRecyclerViewItemPosition{
         void selectedItemPosition(ArrayList items);
    }
}
