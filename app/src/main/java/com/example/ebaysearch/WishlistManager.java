package com.example.ebaysearch;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WishlistManager {
    private static WishlistManager instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private List<ItemModel> wishlist;

//    "how to create a Singleton class in android" prompt (20 line).  ChatGPT, 4 Sep. version, OpenAI, 20 Nov. 2023, chat.openai.com/chat.

    private WishlistManager(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
        wishlist = new ArrayList<>();
    }

    public static synchronized WishlistManager getInstance(Context context) {
        if (instance == null) {
            instance = new WishlistManager(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

//    "how to add an item to wishlist using Singleton Class" prompt (5 line).  ChatGPT, 4 Sep. version, OpenAI, 20 Nov. 2023, chat.openai.com/chat.
    public void addItemToWishlist(ItemModel item, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = "https://ebay-backend-404323.wl.r.appspot.com/api/favorite";

        JSONObject postData = new JSONObject();
        try {
            postData.put("_id", item.getItemId());
            postData.put("image", item.getImage());
            postData.put("title", item.getTitle());
            postData.put("price", item.getPrice());
            postData.put("shipping", item.getShipping());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, listener, errorListener);
        requestQueue.add(jsonObjectRequest);

        // Add item to local wishlist
        wishlist.add(item);
    }

//    "how to remove an item from wishlist using Singleton Class" prompt (5 line).  ChatGPT, 4 Sep. version, OpenAI, 20 Nov. 2023, chat.openai.com/chat.

    public void removeItemFromWishlist(String itemId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = "https://ebay-backend-404323.wl.r.appspot.com/api/favorite/" + itemId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, listener, errorListener);
        requestQueue.add(jsonObjectRequest);

        // Remove item from local wishlist
        for (Iterator<ItemModel> iterator = wishlist.iterator(); iterator.hasNext();) {
            ItemModel item = iterator.next();
            if (item.getItemId().equals(itemId)) {
                iterator.remove();
            }
        }
    }

    public void addItem(ItemModel item) {
        wishlist.add(item);
    }

    public List<ItemModel> getWishlist() {
        return wishlist;
    }
}
