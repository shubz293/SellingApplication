package com.majorproject.groceryshopping.sellingapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubhank Dubey on 07-04-2017.
 */

class PlacedOrder {
    String orderID;
    String customerName;
    String customerAddress;
    int totalAmount;
    boolean status;
    List <ListItem> cartItem = new ArrayList<>();

    public PlacedOrder() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public PlacedOrder(String orderID, String customerName, String customerAddress, int total, boolean status, List<ListItem> cartItem) {
        this.orderID = orderID;

        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.totalAmount = total;
        this.status = status;
        this.cartItem = cartItem;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int total) {
        this.totalAmount = total;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ListItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<ListItem> cartItem) {
        this.cartItem = cartItem;
    }

    public void printAll()
    {
        System.out.println(
                "\n Name\t "+getCustomerName()+
                        "\n Address\t "+getCustomerAddress()+
                        "\n Total\t "+getTotalAmount()+
                        "\n Status\t "+isStatus());
                        printList();
    }

    public void printList()
    {
        System.out.println("List ");
        for(int i =0; i< getCartItem().size(); i++)
            getCartItem().get(i).printAll();
    }

    public int getCartItemSize()
    {
        return getCartItem().size();
    }
}
