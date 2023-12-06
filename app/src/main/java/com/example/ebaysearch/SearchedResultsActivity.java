package com.example.ebaysearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchedResultsActivity extends AppCompatActivity {

    private JSONArray searchItems;
    private RecyclerView recyclerView;
    private SearchItemAdapter adapter;

    private CardView noResultsCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_results);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        noResultsCardView = findViewById(R.id.noResultsFound);

        String searchedResultsString = getIntent().getStringExtra("items");
        Log.d("SEARCH_API.INTENT", searchedResultsString);

        if (searchedResultsString.equals("[]")) {
            noResultsCardView.setVisibility(View.VISIBLE);
        }



        ArrayList<ItemModel> itemList = new ArrayList<>();
        try {
            searchItems = new JSONArray(searchedResultsString);
            for (int i = 0; i < searchItems.length(); i++) {
                JSONObject itemObj = searchItems.getJSONObject(i);
                String itemId = itemObj.getString("itemId");
                String image = itemObj.getString("image");
                String link = itemObj.getString("link");
                String title = itemObj.getString("title");
                String price = itemObj.getString("price");
                String shipping = itemObj.getString("shipping");
                String zip = itemObj.getString("zip");
                JSONObject sellerInfoObj = itemObj.getJSONObject("sellerInfo");
                String sellerInfo = sellerInfoObj.toString();
                JSONObject shippingInfoObj = itemObj.getJSONObject("shippingInfo");
                String shippingInfo = shippingInfoObj.toString();

                ItemModel item = new ItemModel(itemId, image, link, title, price,
                        shipping, zip, sellerInfo, shippingInfo);


                itemList.add(item);
            }
        } catch (JSONException e) {
            Log.e("SEARCH_API.INTENT", "Error parsing searchedResults JSON", e);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildLayoutPosition(view);
                ItemModel item = itemList.get(position);
                Intent intent = new Intent(SearchedResultsActivity.this, SingleItemActivity.class);
                intent.putExtra("itemData", item);
                startActivity(intent);
            }
        };

        adapter = new SearchItemAdapter(itemList, clickListener);
        recyclerView.setAdapter(adapter);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and return to the previous one
            }
        });
    }

    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}