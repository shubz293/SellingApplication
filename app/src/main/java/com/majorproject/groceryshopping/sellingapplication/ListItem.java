package com.majorproject.groceryshopping.sellingapplication;

import android.media.Image;
import android.text.TextUtils;

/**
 * Created by Shubhank Dubey on 31-03-2017.
 */

class ListItem {

    /*Image img;*/
    private String itemName;
    private String price;
    private String offer;
    private String sellerName;
    private String imageUrl;


    public ListItem() {
    }

    public ListItem(String itemName, String price, String offer, String sellerName, String imageUrl) {
        this.itemName = itemName;
        this.price = price;
        this.offer = offer;
        this.sellerName = sellerName;
        this.imageUrl = imageUrl;

    }

    public void setItemName(String itemName) {

        this.itemName = itemName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPrice() {
        return price;
    }

    public String getOffer() {
        return offer;
    }

    public String getSellerName() {
        return sellerName;
    }

    public boolean checkEmpty()
    {
        return TextUtils.isEmpty(getImageUrl()) ||
                TextUtils.isEmpty(getItemName()) ||
                TextUtils.isEmpty(getOffer()) ||
                TextUtils.isEmpty(getPrice()) ||
                TextUtils.isEmpty(getSellerName());
    }

    public void printAll()
    {
        System.out.println(
                "  Name\t: "+getItemName()+
                        "  URL\t: "+getImageUrl()+
                        "  Offer\t: "+getOffer()+
                        "  Price\t: "+getPrice()+
                        "  Seller\t: "+getSellerName());
    }
}
