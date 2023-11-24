package com.example.ebaysearch;

import java.io.Serializable;

public class SearchItemModel implements Serializable {
    private String itemId;
    private String image;
    private String link;
    private String title;
    private String price;
    private String shipping;
    private String zip;
    private String sellerInfo;
    private String shippingInfo;

    public SearchItemModel(String itemId, String image, String link, String title, String price, String shipping, String zip, String sellerInfo, String shippingInfo) {
        this.itemId = itemId;
        this.image = image;
        this.link = link;
        this.title = title;
        this.price = price;
        this.shipping = shipping;
        this.zip = zip;
        this.sellerInfo = sellerInfo;
        this.shippingInfo = shippingInfo;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(String sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public String getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(String shippingInfo) {
        this.shippingInfo = shippingInfo;
    }
}
