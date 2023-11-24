package com.example.ebaysearch.fragments_main_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ebaysearch.R;
import com.example.ebaysearch.SearchItemModel;
import com.example.ebaysearch.SearchedResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class SearchFragment extends Fragment {

    private EditText keywordEditText;

    private TextView keywordErrorTextView;
    private Spinner categorySpinner;
    private CheckBox newCheckBox;
    private CheckBox usedCheckBox;
    private CheckBox unspecifiedCheckBox;
    private CheckBox localPickupCheckBox;
    private CheckBox freeShippingCheckBox;
    private CheckBox nearbySearchCheckBox;
    private LinearLayout linearLayoutDistance;
    private EditText milesEditText;

    private RadioButton currentLocationRadioButton;

    private RadioButton zipCodeRadioButton;
    private EditText zipCodeEditText;

    private TextView zipcodeErrorTextView;

    private SearchItemModel singleItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Find views by their IDs
        keywordEditText = view.findViewById(R.id.editTextText);
        keywordErrorTextView = view.findViewById(R.id.keywordErrorTextView);
        categorySpinner = view.findViewById(R.id.spinner);
        newCheckBox = view.findViewById(R.id.newCheckBox);
        usedCheckBox = view.findViewById(R.id.usedCheckBox);
        unspecifiedCheckBox = view.findViewById(R.id.unspecifiedCheckBox);
        localPickupCheckBox = view.findViewById(R.id.localPickupCheckBox);
        freeShippingCheckBox = view.findViewById(R.id.freeShippingCheckBox);
        nearbySearchCheckBox = view.findViewById(R.id.nearbySearchCheckBox);
        linearLayoutDistance = view.findViewById(R.id.linearLayoutDistance);
        milesEditText = view.findViewById(R.id.editTextMiles);
        currentLocationRadioButton = view.findViewById(R.id.currentLocationRadioButton);
        zipCodeRadioButton = view.findViewById(R.id.zipcodeRadioButton);
        zipCodeEditText = view.findViewById(R.id.editTextZipcode);
        zipcodeErrorTextView = view.findViewById(R.id.zipcodeErrorTextView);


        Button searchButton = view.findViewById(R.id.searchButton);
        Button clearButton = view.findViewById(R.id.clearButton);

        nearbySearchCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                linearLayoutDistance.setVisibility(View.VISIBLE);
            } else {
                linearLayoutDistance.setVisibility(View.GONE);
            }
        });


        // Set listeners for buttons if needed
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform search using the values from the views
                performSearch();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear values in the views
                clearSearchFields();
            }
        });

        return view;
    }

    private void performSearch() {
        // Retrieve values from views and perform search logic
        String keyword = keywordEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        boolean isNew = newCheckBox.isChecked();
        boolean isUsed = usedCheckBox.isChecked();
        boolean isUnspecified = unspecifiedCheckBox.isChecked();
        boolean isLocalPickup = localPickupCheckBox.isChecked();
        boolean isFreeShipping = freeShippingCheckBox.isChecked();
        boolean isNearbySearch = nearbySearchCheckBox.isChecked();
        String miles = milesEditText.getText().toString();
        boolean isCurrentLocation = currentLocationRadioButton.isChecked();
        boolean isZipcode = zipCodeRadioButton.isChecked();
        String zipcode = zipCodeEditText.getText().toString();



        if (keyword.isEmpty()) {
            Log.d("FORM_CHECK.KEYWORD", "performSearch: keyword is empty");
            keywordErrorTextView.setVisibility(View.VISIBLE);
            if (isNearbySearch && isZipcode && zipcode.isEmpty()) {
                zipcodeErrorTextView.setVisibility(View.VISIBLE);
            }
            return;
        }



        miles = miles.isEmpty() ? "10" : miles;
        zipcode = zipcode.isEmpty() ? "90007" : zipcode;

        Log.d("FORM_CHECK", "FORM CHECK PASSED ");

        HashMap<String, String> categoryValues = new HashMap<>();
        categoryValues.put("Art", "550");
        categoryValues.put("Baby", "2984");
        categoryValues.put("Books", "267");
        categoryValues.put("Clothing, Shoes & Accessories", "11450");
        categoryValues.put("Computers/Tablets & Networking", "58058");
        categoryValues.put("Health & Beauty", "26395");
        categoryValues.put("Music", "11233");
        categoryValues.put("Video Games & Consoles", "1249");


        String baseUrl = "https://ebay-backend-404323.wl.r.appspot.com/api/search";

        Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
        builder.appendQueryParameter("keyword", keyword);
        builder.appendQueryParameter("distance", miles);
        builder.appendQueryParameter("zipcode", zipcode);



        if (!category.equals("All"))
            builder.appendQueryParameter("category", categoryValues.get(category));

        if (isNew)
            builder.appendQueryParameter("new", "true");
        if (isUsed)
            builder.appendQueryParameter("used", "true");
        if (isLocalPickup)
            builder.appendQueryParameter("localpickup", "true");
        if (isFreeShipping)
            builder.appendQueryParameter("freeshipping", "true");


        String url = builder.build().toString();
        Log.d("SEARCH_API.URL", url);

        Context context = getContext();
        if (context != null) {
            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String ack = response.getString("ack");
                                JSONArray items = response.getJSONArray("items");
                                // Now you have your items array
                                Log.d("SEARCH_API.Response", ack + " with " + items.length() + " items");
                                Log.d("SEARCH_API.Response", items.toString());

                                Intent intent = new Intent(getContext(), SearchedResults.class);
                                intent.putExtra("items", items.toString());
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("SEARCH_API.Error", error.toString());
                        }
                    });

            queue.add(jsonObjectRequest);

        }
    }

    private void clearSearchFields() {
        keywordEditText.setText("");
        keywordErrorTextView.setVisibility(View.GONE);
        categorySpinner.setSelection(0);
        newCheckBox.setChecked(false);
        usedCheckBox.setChecked(false);
        unspecifiedCheckBox.setChecked(false);
        localPickupCheckBox.setChecked(false);
        freeShippingCheckBox.setChecked(false);
        nearbySearchCheckBox.setChecked(false);
        milesEditText.setText("");
        currentLocationRadioButton.setChecked(false);
        zipCodeRadioButton.setChecked(false);
        zipCodeEditText.setText("");
        zipcodeErrorTextView.setVisibility(View.GONE);

    }
}