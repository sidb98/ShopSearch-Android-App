package com.example.ebaysearch;

import java.io.Serializable;

public class ItemModel implements Serializable {
    private String itemId;
    private String image;
    private String link;
    private String title;
    private String price;
    private String shipping;
    private String zip;
    private String sellerInfo;
    private String shippingInfo;
    private String daysLeft;

//    For Searched Results
    public ItemModel(String itemId, String image, String link, String title, String price, String shipping, String zip, String sellerInfo, String shippingInfo) {
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

//    For Similar Items
    public ItemModel(String itemId, String title, String price, String shipping, String image, String link, String daysLeft) {
        this.itemId = itemId;
        this.image = image;
        this.link = link;
        this.title = title;
        this.price = price;
        this.shipping = shipping;
        this.daysLeft = daysLeft;
    }

//    For Wishlist Items
    public ItemModel(String itemId, String image, String title, String price, String shipping) {
        this.itemId = itemId;
        this.image = image;
        this.title = title;
        this.price = price;
        this.shipping = shipping;
    }

    public String getItemId() {
        return itemId;
    }


    public String getImage() {
        return image;
    }


    public String getLink() {
        return link;
    }


    public String getTitle() {
        return title;
    }


    public String getPrice() {
        return price;
    }


    public String getShipping() {
        return shipping;
    }


    public String getZip() {
        return zip;
    }


    public String getSellerInfo() {
        return sellerInfo;
    }


    public String getShippingInfo() {
        return shippingInfo;
    }


    public String getDaysLeft() {
        return daysLeft;
    }


    @Override
    public String toString() {
        return "SearchItemModel{" +
                "itemId='" + itemId + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", shipping='" + shipping + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", daysLeft='" + daysLeft + '\'' +
                ", zip='" + zip + '\'' +
                ", sellerInfo='" + sellerInfo + '\'' +
                ", shippingInfo='" + shippingInfo + '\'' +
                '}';
    }
}
