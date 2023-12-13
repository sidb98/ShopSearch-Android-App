package com.example.ebaysearch.fragments_single_item_activity;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    private TextView textViewFeedbackScore, textViewPopularity, textViewShippingCost, textViewGlobalShipping,
            textViewHandlingTime, textViewReturnPolicy, textViewStoreName;
    private ImageView imageFeedbackStar;

    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);

        textViewFeedbackScore = view.findViewById(R.id.textViewFeedbackScore);
        progressBar = view.findViewById(R.id.progressBar);
        textViewPopularity = view.findViewById(R.id.progressBarTextView);
        textViewShippingCost = view.findViewById(R.id.textViewShippingCost);
        textViewGlobalShipping = view.findViewById(R.id.textViewGlobalShipping);
        textViewHandlingTime = view.findViewById(R.id.textViewHandlingTime);
        textViewReturnPolicy = view.findViewById(R.id.textViewReturnPolicy);
        textViewStoreName = view.findViewById(R.id.textViewStoreName);
        imageFeedbackStar = view.findViewById(R.id.imageViewFeedbackStar);



        ViewModelItem itemViewModel = new ViewModelProvider(requireActivity()).get(ViewModelItem.class);
        itemViewModel.getItemData().observe(getViewLifecycleOwner(), item -> {
            this.item = item;

            Log.d("ShippingFragment", "onCreateView: " + item.getShippingInfo());
            Log.d("ShippingFragment", "onCreateView: " + item.getSellerInfo());

            try {
                JSONObject shippingInfo = new JSONObject(item.getShippingInfo());
                JSONObject sellerInfo = new JSONObject(item.getSellerInfo());

                Log.d("SellerInfo", "onCreateView: " + sellerInfo.toString());

                String storeUrl = sellerInfo.getString("buyProductAt");
                String storeName = sellerInfo.getString("storeName");

                textViewFeedbackScore.setText(sellerInfo.getString("feedbackScore"));

                Log.d("ShippingFragment", "onCreateView: " + sellerInfo.getString("feedbackRating"));

                if (Float.parseFloat(sellerInfo.getString("feedbackScore")) <= 10000) {
                    imageFeedbackStar.setImageResource(R.drawable.star_circle_outline);
                } else {
                    imageFeedbackStar.setImageResource(R.drawable.star_circle);
                }


                int tint;
                if (sellerInfo.getString("feedbackRating").equals("Yellow")) {
                    tint = ContextCompat.getColor(getContext(), R.color.yellow);
                } else if (sellerInfo.getString("feedbackRating").equals("Blue")) {
                    tint = ContextCompat.getColor(getContext(), R.color.black);
                } else if (sellerInfo.getString("feedbackRating").equals("Turquoise")) {
                    tint = ContextCompat.getColor(getContext(), R.color.yellow);
                } else if (sellerInfo.getString("feedbackRating").equals("Purple")) {
                    tint = ContextCompat.getColor(getContext(), R.color.purple);
                } else if (sellerInfo.getString("feedbackRating").equals("Red")) {
                    tint = ContextCompat.getColor(getContext(), R.color.red);
                } else if (sellerInfo.getString("feedbackRating").equals("Green")) {
                    tint = ContextCompat.getColor(getContext(), R.color.green);
                } else {
                    tint = ContextCompat.getColor(getContext(), R.color.black);
                }

                imageFeedbackStar.setColorFilter(tint, PorterDuff.Mode.SRC_IN);

                textViewPopularity.setText(sellerInfo.getString("popularity"));
                float popularity = Float.parseFloat(sellerInfo.getString("popularity"));
                progressBar.setProgress(Math.round(popularity));

                textViewShippingCost.setText(shippingInfo.getString("shippingCost"));
                textViewGlobalShipping.setText(shippingInfo.getString("shippingLocation"));
                textViewHandlingTime.setText(shippingInfo.getString("handlingTime"));

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