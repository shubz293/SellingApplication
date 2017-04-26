package com.majorproject.groceryshopping.sellingapplication;

/**
 * Created by Shubhank Dubey on 28-03-2017.
 */

class SellerDetails {
    private String sellerName;
    private String contact;
    private String address;
    private int itemCount;
    private String email;

    public SellerDetails()
    {
        this("no data","no data","no data",0,"no data");

    }

    public SellerDetails(String sellerName, String contact, String address, int itemCount, String email) {
        this.sellerName = sellerName;
        this.contact = contact;
        this.address = address;
        this.itemCount = itemCount;
        this.email = email;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getEmail() {
        return email;
    }

    public void printAll()
    {
        System.out.println("sellerName\t: " +sellerName+
                "\ncontact\t: " +contact+
                "\naddress\t: " +address+
                "\nitemCount\t: " +itemCount+
                "\nemail\t: "+email);
    }
}
