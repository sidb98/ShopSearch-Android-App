package com.example.ebaysearch;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class ViewModelSingleItem extends ViewModel{

    private final MutableLiveData<ItemModel> itemData = new MutableLiveData<>();
    private final MutableLiveData<JSONObject> singleItemData = new MutableLiveData<>();
    public void setItemData(ItemModel data) {
        itemData.setValue(data);
    }

    public MutableLiveData<ItemModel> getItemData() {
        return itemData;
    }

    public void setSingleItemData(JSONObject data) {
        singleItemData.setValue(data);
    }

    public MutableLiveData<JSONObject> getSingleItemData() {
        return singleItemData;
    }


}
