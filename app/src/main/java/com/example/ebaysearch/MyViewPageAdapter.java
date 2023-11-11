package com.example.ebaysearch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ebaysearch.fragments.SearchFragment;
import com.example.ebaysearch.fragments.WishlistFragment;

public class MyViewPageAdapter extends FragmentStateAdapter {

    public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new WishlistFragment();
                default:
                    return new SearchFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
