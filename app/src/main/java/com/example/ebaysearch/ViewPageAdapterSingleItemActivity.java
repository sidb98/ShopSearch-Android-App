package com.example.ebaysearch;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ebaysearch.fragments_single_item_activity.PhotosFragment;
import com.example.ebaysearch.fragments_single_item_activity.ProductFragment;
import com.example.ebaysearch.fragments_single_item_activity.ShippingFragment;
import com.example.ebaysearch.fragments_single_item_activity.SimilarFragment;

import org.json.JSONObject;

public class ViewPageAdapterSingleItemActivity extends FragmentStateAdapter {
    private JSONObject singleItemData;

    public ViewPageAdapterSingleItemActivity(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    public void setSingleItemData(JSONObject data) {
        singleItemData = data;
        Log.d("VIEW_PAGER.Set_Data", "singleItemData: " + singleItemData.toString());

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;
        switch (position){
            case 1:
                fragment = new ShippingFragment();
                break;
            case 2:
                fragment = new PhotosFragment();
                break;
            case 3:
                fragment = new SimilarFragment();
                break;
            default:
                fragment = new ProductFragment();
                break;
        }
        return fragment;
    }


    @Override
    public int getItemCount() {
        return 4;
    }
}
