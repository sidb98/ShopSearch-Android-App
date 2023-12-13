package com.example.ebaysearch.fragments_single_item_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ebaysearch.R;
import com.example.ebaysearch.ItemModel;
import com.example.ebaysearch.ViewModelItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ProductFragment extends Fragment {

    private TextView textViewTitle, textViewPrice, textViewPriceWithShipping, textViewBrand;
    private LinearLayout linearLayoutparent, progressBarLayout, galleryLinearLayout;
    private LayoutInflater mInflater;
    private List<String> imageUrls = new ArrayList<>();
    private JSONObject singleItemData, ItemSpecifics;
    private JSONArray productImg;
    private String link, price, location, returnPolicy;
    private ItemModel item;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        linearLayoutparent = view.findViewById(R.id.linearLayoutParent);
        galleryLinearLayout = view.findViewById(R.id.id_gallery);
        progressBarLayout = view.findViewById(R.id.progressBarLayout);


        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewPriceWithShipping = view.findViewById(R.id.textViewPriceWithShipping);
        textViewPrice = view.findViewById(R.id.textViewPrice);
        textViewBrand = view.findViewById(R.id.textViewBrand);



        ViewModelItem itemViewModel = new ViewModelProvider(requireActivity()).get(ViewModelItem.class);
        itemViewModel.getItemData().observe(getViewLifecycleOwner(), item -> {
            // Use the item data here
            this.item = item;
            // Update your UI based on 'item'
            textViewTitle.setText(item.getTitle());
            textViewPriceWithShipping.setText(item.getPrice() + " with " + item.getShipping());

        });

        itemViewModel.getSingleItemData().observe(getViewLifecycleOwner(), singleItemData -> {
            // Use the singleItemData here
            this.singleItemData = singleItemData;

            productImg = singleItemData.optJSONArray("productImg");
            link = singleItemData.optString("link");
            price = singleItemData.optString("Price");
            location = singleItemData.optString("Location");
            returnPolicy = singleItemData.optString("Return");
            ItemSpecifics = singleItemData.optJSONObject("ItemSpecs");

            initGalleryData(productImg);
            initViewGallery();

            // Update your UI based on 'singleItemData'
            textViewPrice.setText(price);
            textViewBrand.setText(ItemSpecifics.optString("Brand"));
            if (ItemSpecifics != null) {
                Iterator<String> keys = ItemSpecifics.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    if (key.equals("Brand")) {
                        continue;
                    }
                    try {
                        String value = ItemSpecifics.getString(key);

                        TextView textView = new TextView(getContext());
                        textView.setText("- " + value);
                        textView.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        linearLayoutparent.addView(textView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        return view;
    }


    private void initViewGallery() {
        mInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < imageUrls.size(); i++) {
            View view = mInflater.inflate(R.layout.gallery_item, galleryLinearLayout, false);
            ImageView imageView = view.findViewById(R.id.id_index_gallery_item_image);
            Picasso.get().load(imageUrls.get(i)).into(imageView);
            galleryLinearLayout.addView(view);

            linearLayoutparent.setVisibility(View.VISIBLE);
            progressBarLayout.setVisibility(View.GONE);

        }
    }

    private void initGalleryData(JSONArray images) {
        if (images != null) {
            for (int i = 0; i < images.length(); i++) {
                try {
                    String url = images.getString(i);
                    imageUrls.add(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}