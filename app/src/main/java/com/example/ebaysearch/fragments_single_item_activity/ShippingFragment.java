package com.example.ebaysearch.fragments_single_item_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ebaysearch.R;
import com.example.ebaysearch.ItemModel;
import com.example.ebaysearch.ViewModelItem;

import org.json.JSONException;
import org.json.JSONObject;


public class ShippingFragment extends Fragment {

    private ItemModel item;

    private JSONObject singleItemData;
    String returnPolicy;

    TextView textViewFeedbackScore, textViewPopularity, textViewShippingCost, textViewGlobalShipping,
            textViewHandlingTime, textViewReturnPolicy, textViewStoreName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);

        textViewFeedbackScore = view.findViewById(R.id.textViewFeedbackScore);
        textViewPopularity = view.findViewById(R.id.textViewPopularity);
        textViewShippingCost = view.findViewById(R.id.textViewShippingCost);
        textViewGlobalShipping = view.findViewById(R.id.textViewGlobalShipping);
        textViewHandlingTime = view.findViewById(R.id.textViewHandlingTime);
        textViewReturnPolicy = view.findViewById(R.id.textViewReturnPolicy);
        textViewStoreName = view.findViewById(R.id.textViewStoreName);

        ViewModelItem itemViewModel = new ViewModelProvider(requireActivity()).get(ViewModelItem.class);
        itemViewModel.getItemData().observe(getViewLifecycleOwner(), item -> {
            // Use the item data here
            this.item = item;

            Log.d("ShippingFragment", "onCreateView: " + item.getShippingInfo());
            Log.d("ShippingFragment", "onCreateView: " + item.getSellerInfo());

            try {
                JSONObject shippingInfo = new JSONObject(item.getShippingInfo());
                JSONObject sellerInfo = new JSONObject(item.getSellerInfo());

                String storeUrl = sellerInfo.getString("buyProductAt");
                String storeName = sellerInfo.getString("storeName");

                textViewFeedbackScore.setText(sellerInfo.getString("feedbackScore"));
                textViewPopularity.setText(sellerInfo.getString("popularity"));
                textViewShippingCost.setText(shippingInfo.getString("shippingCost"));
                textViewGlobalShipping.setText(shippingInfo.getString("shippingLocation"));
                textViewHandlingTime.setText(shippingInfo.getString("handlingTime"));

//                TODO: Find the store name in the string and make it clickable

                SpannableString spannableString = new SpannableString(storeName);
                spannableString.setSpan(new URLSpan(storeUrl), 0, storeName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                textViewStoreName.setText(spannableString);
                textViewStoreName.setMovementMethod(LinkMovementMethod.getInstance());


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        itemViewModel.getSingleItemData().observe(getViewLifecycleOwner(), singleItemData -> {
            // Use the item data here
            this.singleItemData = singleItemData;

            returnPolicy = singleItemData.optString("Return");
            textViewReturnPolicy.setText(returnPolicy);

        });

        return view;

    }
}