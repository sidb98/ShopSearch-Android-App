package com.example.ebaysearch;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class SimilarItemAdapter extends RecyclerView.Adapter<SimilarItemAdapter.ViewHolder> {
    private List<ItemModel> similarItems;

    public SimilarItemAdapter(List<ItemModel> similarItems) {
        this.similarItems = similarItems;
    }
    @NonNull
    @Override
    public SimilarItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_item_recycler_view_row, parent, false);
        return new ViewHolder(view);    }


    @Override
    public void onBindViewHolder(@NonNull SimilarItemAdapter.ViewHolder holder, int position) {
        ItemModel item = similarItems.get(position);
        Log.d("SimilarItemAdapter", "onBindViewHolder: " + item.getTitle() + " " + item.getPrice() + " " + item.getShipping() + " " + item.getDaysLeft() + " " + item.getImage());
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewPrice.setText(item.getPrice());
        holder.textViewShipping.setText(item.getShipping());
        holder.textViewDaysLeft.setText(String.format(item.getDaysLeft())+" Days Left");


        Picasso.get().load(item.getImage()).into(holder.imageViewItem);

        // Optionally, if you want to handle item clicks:
        holder.itemView.setOnClickListener(view -> {
            // Handle the item click event
            // This could be a callback to the activity or fragment
        });

    }

    @Override
    public int getItemCount() {
        return similarItems.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewItem;
        public TextView textViewTitle, textViewPrice, textViewShipping, textViewDaysLeft;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.item_image);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewUnderDaysLeft);
            textViewShipping = itemView.findViewById(R.id.textViewShipping);
            textViewDaysLeft = itemView.findViewById(R.id.textViewDaysLeft);
        }
    }

}
