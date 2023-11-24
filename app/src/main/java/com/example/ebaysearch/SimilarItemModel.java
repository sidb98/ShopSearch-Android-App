package com.example.ebaysearch;

public class SimilarItemModel {
    private String itemId;
    private String title;
    private String price;
    private String shipping;
    private String image;
    private String link;
    private String daysLeft;


    public SimilarItemModel(String itemId, String title, String price, String shipping, String image, String link, String daysLeft) {
        this.itemId = itemId;
        this.title = title;
        this.price = price;
        this.shipping = shipping;
        this.image = image;
        this.link = link;
        this.daysLeft = daysLeft;
    }


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(String daysLeft) {
        this.daysLeft = daysLeft;
    }

}
