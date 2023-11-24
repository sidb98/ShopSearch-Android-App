package com.example.ebaysearch;

public class WishlistItemModel {
    private String itemId;
    private String image;
    private String title;
    private String price;
    private String shipping;

    public WishlistItemModel(String itemId, String image, String title, String price, String shipping) {
        this.itemId = itemId;
        this.image = image;
        this.title = title;
        this.price = price;
        this.shipping = shipping;
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
}
