package com.example.ebaysearch.fragments_main_activity;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ebaysearch.ItemModel;
import com.example.ebaysearch.R;
import com.example.ebaysearch.WishlistAdapter;
import com.example.ebaysearch.WishlistManager;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment implements OnWishlistChangeListener {
    private RecyclerView recyclerView;
    private List<ItemModel> wishlist = new ArrayList<>();
    private WishlistAdapter adapter;
    private CardView emptyWishlistCardView;
    private LinearLayout wishlistLinearLayout;
    private TextView itemCount, totalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        emptyWishlistCardView = view.findViewById(R.id.emptyWishlistCardView);
        wishlistLinearLayout = view.findViewById(R.id.wishlistBottomLinearLayout);

        itemCount = view.findViewById(R.id.wishlistItemCountTextView);
        totalPrice = view.findViewById(R.id.wishlistTotalPriceTextView);

        recyclerView = view.findViewById(R.id.wishlistRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateWishlist();
    }

    @Override
    public void onWishlistChanged() {
        updateWishlist();
    }

    private void updateWishlist() {
        wishlist = WishlistManager.getInstance(getContext()).getWishlist();
        updateUIBasedOnWishlist();
        adapter = new WishlistAdapter(wishlist);
        adapter.setOnWishlistChangeListener(this);
        recyclerView.setAdapter(adapter);
    }


    private void updateUIBasedOnWishlist() {
        if (wishlist.isEmpty()) {
            emptyWishlistCardView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            wishlistLinearLayout.setVisibility(View.GONE);
        } else {
            emptyWishlistCardView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            wishlistLinearLayout.setVisibility(View.VISIBLE);

            itemCount.setText("Wishlist Total (" + wishlist.size() + " items)");
            double total = 0;
            for (ItemModel item : wishlist) {
                total += Float.parseFloat(item.getPrice());
            }
            totalPrice.setText("$" + String.format("%.2f", total));
        }
    }
}

