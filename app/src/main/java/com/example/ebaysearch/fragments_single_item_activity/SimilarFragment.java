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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ebaysearch.R;
import com.example.ebaysearch.ItemModel;
import com.example.ebaysearch.SimilarItemAdapter;
import com.example.ebaysearch.ViewModelItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SimilarFragment extends Fragment {

    private ItemModel item;

    private List<ItemModel> similarItemsList, defaultSimilarItemsList;

    private LinearLayout mainContentLayout, progressBarLayout;
    private Spinner sort_category, sort_order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_similar, container, false);
        ViewModelItem itemViewModel = new ViewModelProvider(requireActivity()).get(ViewModelItem.class);

        mainContentLayout = view.findViewById(R.id.mainContentLayout);
        progressBarLayout = view.findViewById(R.id.progressBarLayout);

        sort_category = view.findViewById(R.id.spinnerCategory);
        sort_order = view.findViewById(R.id.spinnerOrder);

        sort_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sort_order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


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

                        similarItemsList = new ArrayList<>();
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


                                ItemModel similarItem = new ItemModel(itemId, title, price, shipping, image, link, daysLeft);
                                similarItemsList.add(similarItem);
                            }
                            defaultSimilarItemsList = new ArrayList<>(similarItemsList);
//                             Update RecyclerView here, if this code is on the UI thread
                            setupRecyclerView(similarItemsList);
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

    private void setupRecyclerView(List<ItemModel> itemsList) {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SimilarItemAdapter adapter = new SimilarItemAdapter(itemsList);
        recyclerView.setAdapter(adapter);

        progressBarLayout.setVisibility(View.GONE);
        mainContentLayout.setVisibility(View.VISIBLE);

    }


    private void sortItems() {
        if (similarItemsList == null || similarItemsList.isEmpty()) {
            return;
        }

        String category = sort_category.getSelectedItem().toString();
        String order = sort_order.getSelectedItem().toString();

        if (category.equals("Default")) {
            // Reset to original list
            similarItemsList = new ArrayList<>(defaultSimilarItemsList);
            setupRecyclerView(similarItemsList);
            return;
        }

        Comparator<ItemModel> comparator = null;

        switch (category) {
            case "Name":
                comparator = Comparator.comparing(ItemModel::getTitle);
                break;
            case "Price":
                comparator = Comparator.comparingDouble(item -> Double.parseDouble(item.getPrice()));
                break;
            case "Days":
                comparator = Comparator.comparingInt(item -> Integer.parseInt(item.getDaysLeft()));
                break;
        }

        if (comparator != null) {
            if (order.equals("Descending")) {
                comparator = comparator.reversed();
            }

            Collections.sort(similarItemsList, comparator);
            setupRecyclerView(similarItemsList);
        }
    }

}
