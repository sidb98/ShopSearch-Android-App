package com.example.ebaysearch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ebaysearch.fragments_main_activity.SearchFragment;
import com.example.ebaysearch.fragments_main_activity.WishlistFragment;

public class ViewPageAdapterMainActivity extends FragmentStateAdapter {

    public ViewPageAdapterMainActivity(@NonNull FragmentActivity fragmentActivity) {
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
