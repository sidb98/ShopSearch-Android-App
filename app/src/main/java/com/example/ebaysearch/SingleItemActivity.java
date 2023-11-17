package com.example.ebaysearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ebaysearch.fragments_single_item_activity.ProductFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleItemActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    SearchItemModel item;
    ViewPageAdapterSingleItemActivity viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);
        item = (SearchItemModel) getIntent().getSerializableExtra("itemData");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        viewPagerAdapter = new ViewPageAdapterSingleItemActivity(this);
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Log.d("position", String.valueOf(position));
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        fetchItemDetails(item.getItemId());

    }

    private void fetchItemDetails(String itemId) {
        String url = "https://ebay-backend-404323.wl.r.appspot.com/api/singleItem/" + itemId;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SingleItemActivity", "Response: " + response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("SingleItemActivity", "Error fetching item details: " + error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

}