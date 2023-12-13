package com.example.ebaysearch;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebaysearch.fragments_main_activity.OnWishlistChangeListener;
import com.squareup.picasso.Picasso;


import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<ItemModel> WishlistItem;
    private OnWishlistChangeListener wishlistChangeListener = null;

    public WishlistAdapter(List<ItemModel> item) {
        this.WishlistItem = item;
    }
    public void setOnWishlistChangeListener(OnWishlistChangeListener listener) {
        this.wishlistChangeListener = listener;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_recycler_view_row, parent, false);
        WishlistAdapter.ViewHolder viewHolder = new WishlistAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        ItemModel item = WishlistItem.get(position);
        holder.itemTitle.setText(item.getTitle());
        holder.itemPrice.setText("$" + item.getPrice());
        holder.itemZipcode.setText("Zip: " + item.getZip());
        holder.itemShipping.setText("Shipping: " + item.getShipping());
        // Set image using Glide or Picasso
        Picasso.get()
                .load(item.getImage())
                .error(R.drawable.ic_launcher_background) // optional, shown if there's an error loading the image
                .into(holder.itemImage);

        holder.wishlist_cart.setImageResource(R.drawable.cart_remove);

        holder.wishlist_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currPosition = holder.getAdapterPosition();
                WishlistItem.remove(currPosition);
                notifyItemRemoved(currPosition);
                notifyItemRangeChanged(currPosition, WishlistItem.size());

                WishlistManager.getInstance(v.getContext()).removeItemFromWishlist(item.getItemId(), null, null);

                String title = item.getTitle();
                if (title.length() > 10) {
                    title = title.substring(0, 10) + "...";
                }
                Toast.makeText(v.getContext(), title + " removed from wishlist", Toast.LENGTH_SHORT).show();

                if (wishlistChangeListener != null) {
                    wishlistChangeListener.onWishlistChanged();
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return WishlistItem.size();
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
}
