package com.example.ebaysearch;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

    private ArrayList<SearchItemModel> searchItems;
    private View.OnClickListener clickListener;

    public SearchItemAdapter(ArrayList<SearchItemModel> searchItems, View.OnClickListener clickListener) {
        this.searchItems = searchItems;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_recycler_view_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchItemModel item = searchItems.get(position);
        holder.itemTitle.setText(item.getTitle());
        holder.itemPrice.setText("$" + item.getPrice());
        holder.itemZipcode.setText("Zip: " + item.getZip());
        holder.itemShipping.setText("Shipping: " + item.getShipping());
        // Set image using Glide or Picasso
        Picasso.get()
                .load(item.getImage())
                .error(R.drawable.ic_launcher_background) // optional, shown if there's an error loading the image
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle, itemPrice, itemZipcode, itemShipping;
        public ImageView itemImage;
        // Add other views

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemZipcode = itemView.findViewById(R.id.item_zipcode);
            itemShipping = itemView.findViewById(R.id.item_shipping);
            itemImage = itemView.findViewById(R.id.item_image);
            // Initialize other views
        }
    }
}
