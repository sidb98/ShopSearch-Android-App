package com.example.ebaysearch.fragments_single_item_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.Volley;
import com.example.ebaysearch.R;
import com.example.ebaysearch.ItemModel;
import com.example.ebaysearch.ViewModelItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;



public class PhotosFragment extends Fragment {

    ItemModel item;
    String title;
    ArrayList<String> imageUrls = new ArrayList<>();

    LinearLayout photoLinearLayout, progressBarLayout;
    LayoutInflater mInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        photoLinearLayout = view.findViewById(R.id.photoLinearLayout);
        progressBarLayout = view.findViewById(R.id.progressBarLayout);


        ViewModelItem itemViewModel = new ViewModelProvider(requireActivity()).get(ViewModelItem.class);

        itemViewModel.getItemData().observe(getViewLifecycleOwner(), item -> {
            // Use the item data here
            this.item = item;
            Log.d("PhotosFragment", item.getTitle());
            this.title = item.getTitle();
            callGoogleSearchApi();

        });


        return view;
    }

    private void setPhotos() {

        mInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < imageUrls.size(); i++) {
            View view = mInflater.inflate(R.layout.photo_item, photoLinearLayout, false);
            ImageView imageView = view.findViewById(R.id.id_index_photo_item_image);
            Picasso.get().load(imageUrls.get(i)).into(imageView);
            photoLinearLayout.addView(view);
        }
        progressBarLayout.setVisibility(View.GONE);
        photoLinearLayout.setVisibility(View.VISIBLE);
    }


    private void callGoogleSearchApi() {

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String shortTitle = title.substring(0, Math.min(title.length(), 40));
        String url = "https://ebay-backend-404323.wl.r.appspot.com/api/photos?productTitle=" + shortTitle;

        Log.d("Google_API.Request", url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            imageUrls.clear();
                            for (int i = 0; i < response.length(); i++) {
                                String imageUrl = response.getString(i);
                                imageUrls.add(imageUrl);
                            }
                            Log.d("Google_API.Response", response.toString());
                            setPhotos();
                            // Use imageUrls as needed for your UI
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("Google_API.Error", error.toString());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

}