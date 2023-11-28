package com.example.ebaysearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SingleItemActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private ItemModel item;
    private ViewPageAdapterSingleItemActivity viewPagerAdapter;

    private ViewModelItem itemViewModel;

    private List<ItemModel> wishlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);


        item = (ItemModel) getIntent().getSerializableExtra("itemData");
        itemViewModel = new ViewModelProvider(this).get(ViewModelItem.class);
        itemViewModel.setItemData(item);
        FloatingActionButton fab = findViewById(R.id.fab_wishlist);


        wishlist = WishlistManager.getInstance(getApplicationContext()).getWishlist();
        Log.d("WISHLIST", wishlist.toString());

        if (isItemInWishlist(item.getItemId())) {
            fab.setImageResource(R.drawable.cart_off_white);
        } else {
            fab.setImageResource(R.drawable.cart_plus_white);
        }


        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        ImageButton backButton = findViewById(R.id.backButton);


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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and return to the previous one
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isItemInWishlist = isItemInWishlist(item.getItemId());
                ItemModel wishlistItem = new ItemModel(
                        item.getItemId(), item.getImage(), item.getTitle(), item.getPrice(), item.getShipping()
                );

                if (!isItemInWishlist) {
                    addItemToWishlist(wishlistItem);
                    fab.setImageResource(R.drawable.cart_off_white);
                    Toast.makeText(getApplicationContext(), "Item added to wishlist", Toast.LENGTH_SHORT).show();

                } else {
                    removeItemFromWishlist(item.getItemId());
                    fab.setImageResource(R.drawable.cart_plus_white);
                    Toast.makeText(getApplicationContext(), "Item removed from wishlist", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean isItemInWishlist(String itemId) {
        for (ItemModel item : wishlist) {
            if (item.getItemId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    private void fetchItemDetails(String itemId) {
        String url = "https://ebay-backend-404323.wl.r.appspot.com/api/singleItem/" + itemId;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("SINGLE_ITEM_API", "Response: " + jsonObject.toString());
                            itemViewModel.setSingleItemData(jsonObject);
                        } catch (JSONException e) {
                            Log.e("SINGLE_ITEM_API.ERROR", "JSON Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SINGLE_ITEM.ERROR", "Error: " + error.getMessage());
                    }
                }
        );

        queue.add(stringRequest);
    }

    private void addItemToWishlist(ItemModel item) {
        WishlistManager.getInstance(getApplicationContext()).addItemToWishlist(item, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("WISHLIST_API.Added", "Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WISHLIST_API.ERROR", "Error: " + error.getMessage());
            }
        });
    }

    private void removeItemFromWishlist(String itemId) {
        WishlistManager.getInstance(getApplicationContext()).removeItemFromWishlist(itemId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("WISHLIST_API.Remove", "Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WISHLIST_API.ERROR", "Error: " + error.getMessage());
            }
        });
    }

}