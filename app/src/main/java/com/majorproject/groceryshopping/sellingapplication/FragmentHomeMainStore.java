package com.majorproject.groceryshopping.sellingapplication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeMainStore extends Fragment {


    private Button addItemButton;
    private Button viewItemButton;
    private Button myAccountButton;
    private Button viewOrderButton;

    private SellerDetails sellerDetails = new SellerDetails();



    public FragmentHomeMainStore() {

        // Required empty public constructor
    }

    public static FragmentHomeMainStore getInstance(SellerDetails details)
    {
        FragmentHomeMainStore homeMainStore = new FragmentHomeMainStore();
        homeMainStore.sellerDetails = details;


        return homeMainStore;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home_main_store, container, false);
        rootView.setTag("FragmentHomeMainStore");

        addItemButton = (Button) rootView.findViewById(R.id.button_add_item);
        viewOrderButton = (Button) rootView.findViewById(R.id.button_view_order);
        viewItemButton = (Button) rootView.findViewById(R.id.button_view_item);
        myAccountButton = (Button) rootView.findViewById(R.id.button_my_account);


        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemButtonOnClick();
            }
        });
        viewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewItemButtonOnClick();
            }
        });
        viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOrderButtonOnClick();
            }
        });
        myAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAccountButtonOnClick();
            }
        });



        // Inflate the layout for this fragment
        return rootView;
    }


    private void myAccountButtonOnClick()
    {
        FragmentMyAccountMainSeller account = FragmentMyAccountMainSeller.getInstance(sellerDetails);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.relative_layout_fragment, account)
                .addToBackStack(null)
                .commit();
    }

    private void viewOrderButtonOnClick()
    {
        FragmentViewOrders fragmentViewOrders = FragmentViewOrders.getInstance();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.relative_layout_fragment, fragmentViewOrders)
                .addToBackStack(null)
                .commit();
    }

    private void viewItemButtonOnClick()
    {
        FragmentMyItemsMainSeller fragmentMyItemsMainSeller = FragmentMyItemsMainSeller.getInstance();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.relative_layout_fragment, fragmentMyItemsMainSeller)
                .addToBackStack(null)
                .commit();
    }

    private void addItemButtonOnClick()
    {
        AddItemFragment addItemFragment = AddItemFragment.getInstance(sellerDetails);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.relative_layout_fragment, addItemFragment)
                .commit();
    }


}
