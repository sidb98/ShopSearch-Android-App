package com.example.ebaysearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchedResults extends AppCompatActivity {

    private JSONArray searchItems;
    private RecyclerView recyclerView;
    private SearchItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_results);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        String searchedResultsString = getIntent().getStringExtra("items");
        ArrayList<SearchItemModel> itemList = new ArrayList<>();
        try {
            searchItems = new JSONArray(searchedResultsString);
            for (int i = 0; i < searchItems.length(); i++) {
                JSONObject itemObj = searchItems.getJSONObject(i);
                SearchItemModel  item = new SearchItemModel();
                item.setItemId(itemObj.getString("itemId"));
                item.setImage(itemObj.getString("image"));
                item.setLink(itemObj.getString("link"));
                item.setTitle(itemObj.getString("title"));
                item.setPrice(itemObj.getString("price"));
                item.setShipping(itemObj.getString("shipping"));
                item.setZip(itemObj.getString("zip"));
                JSONObject sellerInfoObj = itemObj.getJSONObject("sellerInfo");
                item.setSellerInfo(sellerInfoObj.toString());
                JSONObject shippingInfoObj = itemObj.getJSONObject("shippingInfo");
                item.setShippingInfo(shippingInfoObj.toString());

                itemList.add(item);
            }
        } catch (JSONException e) {
            Log.e("SEARCH_API.INTENT", "Error parsing searchedResults JSON", e);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildLayoutPosition(view);
                SearchItemModel item = itemList.get(position);
                Intent intent = new Intent(SearchedResults.this, SingleItemActivity.class);
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
}