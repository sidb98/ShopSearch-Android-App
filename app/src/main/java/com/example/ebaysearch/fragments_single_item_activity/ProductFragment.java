package com.example.ebaysearch.fragments_single_item_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ebaysearch.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        Log.d("ProductFragment", "onCreateView:");
        return view;
    }

}