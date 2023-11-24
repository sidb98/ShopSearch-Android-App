package com.example.ebaysearch.fragments_single_item_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ebaysearch.R;
import com.example.ebaysearch.SearchItemModel;
import com.example.ebaysearch.SimilarItemAdapter;
import com.example.ebaysearch.SimilarItemModel;
import com.example.ebaysearch.ViewModelSingleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SimilarFragment extends Fragment {

    private SearchItemModel item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_similar, container, false);
        ViewModelSingleItem itemViewModel = new ViewModelProvider(requireActivity()).get(ViewModelSingleItem.class);
        itemViewModel.getItemData().observe(getViewLifecycleOwner(), item -> {
            // Use the item data here
            this.item = item;
            String itemId = item.getItemId();

            fetchSimilarItems(itemId);

        });
        return view;
    }

    private void fetchSimilarItems(String itemId) {
        String url = "https://ebay-backend-404323.wl.r.appspot.com/api/similarItems/" + itemId;

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Similar_Item.Response", response.toString());

                        List<SimilarItemModel> itemsList = new ArrayList<>();
                        try {
                            JSONArray items = response.getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                String itemId = item.optString("itemId");
                                String title = item.optString("title");
                                String price = item.optString("price");
                                String shipping = item.optString("shipping");
                                String image = item.optString("image");
                                String link = item.optString("link");
                                String daysLeft = item.optString("daysLeft");

                                SimilarItemModel similarItem = new SimilarItemModel(itemId,title, price, shipping, image, link, daysLeft);
                                itemsList.add(similarItem);
                            }
//                             Update RecyclerView here, if this code is on the UI thread
                            setupRecyclerView(itemsList);
                        } catch (JSONException e) {
                            Log.e("Similar_Item.Error", "Json parsing error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                Log.e("Similar_Item.Error", error.toString());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
    private void setupRecyclerView(List<SimilarItemModel> itemsList) {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SimilarItemAdapter adapter = new SimilarItemAdapter(itemsList);
        recyclerView.setAdapter(adapter);
    }
}
