package com.example.ebaysearch;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPageAdapterMainActivity myViewPageAdapter;

    private ViewModelItem viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        myViewPageAdapter = new ViewPageAdapterMainActivity(this);
        viewPager2.setAdapter(myViewPageAdapter);

        viewModel = new ViewModelItem();

        fetchWishListItems();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // When a tab is selected, switch to the corresponding page in the ViewPager.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            // Unused methods.
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
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void fetchWishListItems() {

        String url = "https://ebay-backend-404323.wl.r.appspot.com/api/getKeys";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        WishlistManager wishlistManager = WishlistManager.getInstance(getApplicationContext());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                ItemModel item = new ItemModel(
                                        jsonObject.getString("itemId"),
                                        jsonObject.getString("image"),
                                        jsonObject.getString("title"),
                                        jsonObject.getString("price"),
                                        jsonObject.getString("shipping"));
                                wishlistManager.addItem(item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        error.printStackTrace();
                    }
                });

        queue.add(jsonArrayRequest);
    }

}
