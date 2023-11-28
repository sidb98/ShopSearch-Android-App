package com.example.ebaysearch.fragments_main_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ebaysearch.ItemModel;
import com.example.ebaysearch.R;
import com.example.ebaysearch.WishlistAdapter;
import com.example.ebaysearch.WishlistManager;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<ItemModel> wishlist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_wishlist, container, false);

        wishlist = WishlistManager.getInstance(getContext()).getWishlist();

        recyclerView = view.findViewById(R.id.wishlistRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        WishlistAdapter adapter = new WishlistAdapter(wishlist);
        recyclerView.setAdapter(adapter);



        return view;
    }
}