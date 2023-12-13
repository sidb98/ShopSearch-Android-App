package com.example.ebaysearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

    private ArrayList<ItemModel> searchItems;
    private View.OnClickListener clickListener;

    private List<ItemModel> wishlist;


    public SearchItemAdapter(ArrayList<ItemModel> searchItems, View.OnClickListener clickListener) {
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
        ItemModel item = searchItems.get(position);
        holder.itemTitle.setText(item.getTitle());
        holder.itemPrice.setText("$" + item.getPrice());
        holder.itemZipcode.setText("Zip: " + item.getZip());
        if (item.getShipping().equals("Free Shipping")) {
            holder.itemShipping.setText("Free");
        } else {
            holder.itemShipping.setText("Shipping: $" + item.getShipping());
        }
        Picasso.get()
                .load(item.getImage())
                .error(R.drawable.ic_launcher_background)
                .into(holder.itemImage);

        wishlist = WishlistManager.getInstance(holder.itemView.getContext()).getWishlist();
        if (isItemInWishlist(item.getItemId())) {
            holder.wishlist_cart.setImageResource(R.drawable.cart_remove);
        } else {
            holder.wishlist_cart.setImageResource(R.drawable.cart_plus);
        }

        holder.wishlist_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isItemInWishlist(item.getItemId())) {
                    holder.wishlist_cart.setImageResource(R.drawable.cart_plus);
                    WishlistManager.getInstance(v.getContext()).removeItemFromWishlist(item.getItemId(), null, null);
                    String title = item.getTitle();
                    if (title.length() > 10) {
                        title = title.substring(0, 10) + "...";
                    }
                    Toast.makeText(v.getContext(), title+" removed from wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    holder.wishlist_cart.setImageResource(R.drawable.cart_remove);
                    WishlistManager.getInstance(v.getContext()).addItemToWishlist(item, null, null);
                    String title = item.getTitle();
                    if (title.length() > 10) {
                        title = title.substring(0, 10) + "...";
                    }
                    Toast.makeText(v.getContext(), title+" added to wishlist", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle, itemPrice, itemZipcode, itemShipping;
        public ImageView itemImage, wishlist_cart;
        // Add other views

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemZipcode = itemView.findViewById(R.id.item_zipcode);
            itemShipping = itemView.findViewById(R.id.item_shipping);
            itemImage = itemView.findViewById(R.id.item_image);
            wishlist_cart = itemView.findViewById(R.id.wishlist_cart);
        }
    }

    private boolean isItemInWishlist(String itemId) {
        for (ItemModel item : wishlist) {
            if (item.getItemId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

}
