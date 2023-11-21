package com.example.ebaysearch.fragments_single_item_activity;

import android.net.Uri;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ebaysearch.R;
import com.example.ebaysearch.SearchItemModel;
import com.example.ebaysearch.ViewModelSingleItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PhotosFragment extends Fragment {

    SearchItemModel item;
    String title;
    ArrayList<String> imageUrls = new ArrayList<>();

    LinearLayout photoLinearLayout;
    LayoutInflater mInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        photoLinearLayout = view.findViewById(R.id.photoLinearLayout);

        ViewModelSingleItem itemViewModel = new ViewModelProvider(requireActivity()).get(ViewModelSingleItem.class);
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
    }


    private void callGoogleSearchApi() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://www.googleapis.com/customsearch/v1";
        Log.d("PhotosFragment", "callGoogleSearchApi: " + title);
        Map<String, String> params = new HashMap<>();
        params.put("q", this.title);
        params.put("cx", "051e7706630e941c0"); // Replace with actual CX
        params.put("imageSize", "huge");
        params.put("imageType", "news");
        params.put("num", "8");
        params.put("searchType", "image");
        params.put("key", "AIzaSyAE_wV6ANqE0FIwJ3zTdXud_Oj2co5tqfE");

        String queryParams = "";
        for (String key : params.keySet()) {
            queryParams += "&" + key + "=" + Uri.encode(params.get(key));
        }

        url += "?" + queryParams.substring(1);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                imageUrls.add(item.getString("link"));
                            }
                            Log.d("Google_API.Response", response.toString());
                            setPhotos();
                            // Use itemLinks as needed for your UI
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
        queue.add(jsonObjectRequest);
    }
}