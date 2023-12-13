package com.example.ebaysearch.fragments_main_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ebaysearch.R;
import com.example.ebaysearch.ItemModel;
import com.example.ebaysearch.SearchedResultsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchFragment extends Fragment {

    private EditText keywordEditText, milesEditText;
    private TextView keywordErrorTextView, zipcodeErrorTextView;
    private Spinner categorySpinner;
    private CheckBox newCheckBox, usedCheckBox, unspecifiedCheckBox, localPickupCheckBox, freeShippingCheckBox, nearbySearchCheckBox;
    private RelativeLayout relativeLayoutDistance;
    private RadioButton currentLocationRadioButton, zipCodeRadioButton;
    private AutoCompleteTextView zipCodeAutoCompleteText;

    private LinearLayout mainContentLayout, progressBarLayout;

    private ArrayList<String> zipCodesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


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
        relativeLayoutDistance = view.findViewById(R.id.relativeLayoutDistance);
        milesEditText = view.findViewById(R.id.editTextMiles);
        currentLocationRadioButton = view.findViewById(R.id.currentLocationRadioButton);
        zipCodeRadioButton = view.findViewById(R.id.zipcodeRadioButton);
        zipCodeAutoCompleteText = view.findViewById(R.id.editTextZipcode);
        zipcodeErrorTextView = view.findViewById(R.id.zipcodeErrorTextView);

        mainContentLayout = view.findViewById(R.id.mainContentLayout);
        progressBarLayout = view.findViewById(R.id.progressBarLayout);


        Button searchButton = view.findViewById(R.id.searchButton);
        Button clearButton = view.findViewById(R.id.clearButton);

        nearbySearchCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                relativeLayoutDistance.setVisibility(View.VISIBLE);
            } else {
                relativeLayoutDistance.setVisibility(View.GONE);
            }
        });

        zipCodeAutoCompleteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchZipcodes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                zipCodesList);


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
        String zipcode = zipCodeAutoCompleteText.getText().toString();


        if (keyword.isEmpty()) {
            Log.d("FORM_CHECK.KEYWORD", "performSearch: keyword is empty");
            Toast.makeText(getContext(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
            keywordErrorTextView.setVisibility(View.VISIBLE);
            if (isNearbySearch && isZipcode && zipcode.isEmpty()) {
                zipcodeErrorTextView.setVisibility(View.VISIBLE);
            }
            return;
        }
        else {
            keywordErrorTextView.setVisibility(View.GONE);
        }
        Log.d("FORM_CHECK", "Zipcode" + isNearbySearch + isZipcode + zipcode);
        if (isNearbySearch && isZipcode && zipcode.isEmpty()) {
            zipcodeErrorTextView.setVisibility(View.VISIBLE);
            return;
        }else {
            zipcodeErrorTextView.setVisibility(View.GONE);
        }


        miles = miles.isEmpty() ? "10" : miles;
        zipcode = zipcode.isEmpty() ? "90007" : zipcode;
        Log.d("SEARCH_API.FORM_CHECK.Zipcode", "performSearch: zipcode is " + zipcode);

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

        progressBarLayout.setVisibility(View.VISIBLE);
        mainContentLayout.setVisibility(View.GONE);



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


                                Intent intent = new Intent(getContext(), SearchedResultsActivity.class);
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
        zipCodeAutoCompleteText.setText("");
        zipcodeErrorTextView.setVisibility(View.GONE);

    }

    private void fetchZipcodes(String inputValue) {

        String url = "https://ebay-backend-404323.wl.r.appspot.com/api/geolocation?startsWith=" + inputValue;

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Process the response to extract zip codes
                        // Update your EditText or other UI elements here
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<String> zipCodesList = new ArrayList<>();
                            zipCodesList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                zipCodesList.add(jsonArray.getString(i));
                            }
                            Log.d("ZIPCODES", zipCodesList.toString());
                            // From https://developer.android.com/reference/android/widget/AutoCompleteTextView
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    zipCodesList);
                            zipCodeAutoCompleteText.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("Volley", "Error on fetching zip codes: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        progressBarLayout.setVisibility(View.GONE);
        mainContentLayout.setVisibility(View.VISIBLE);
    }

}