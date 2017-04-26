package com.majorproject.groceryshopping.sellingapplication;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentViewOrders extends Fragment /*implements Openable*/ {

    private ProgressDialog progressDialog ;
    private RecyclerView recyclerView;
    private MyOrderAdapter adapter;

    private DatabaseReference databaseReference;
    private ProgressBar progressBar ;

    private ArrayList<PlacedOrder> orderList;

    public static FragmentViewOrders getInstance()
    {
        FragmentViewOrders fragmentViewOrders = new FragmentViewOrders();

        return fragmentViewOrders;
    }

    public FragmentViewOrders() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Orders..");
        progressDialog.show();

        orderList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_orders, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_orders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = (ProgressBar) view.findViewById(R.id.myOrderProgressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        databaseReference.child(getString(R.string.firebase_order_subclass))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        readData(dataSnapshot);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        return view;
    }

    private void readData(DataSnapshot snap)
    {
        try {
            for (int i=0; i<= snap.getChildrenCount(); i++)
            {
                String v =String.valueOf(i);
                PlacedOrder order = new PlacedOrder();
                order.setOrderID(snap.child(v)
                        .child("orderID")
                        .getValue(String.class));

                order.setCustomerName(snap.child(v)
                        .child("customerName")
                        .getValue(String.class));

                order.setCustomerAddress(snap.child(v)
                        .child("customerAddress")
                        .getValue(String.class));

                order.setTotalAmount(snap.child(v)
                        .child("total")
                        .getValue(int.class));

                order.setStatus(snap.child(v)
                        .child("status")
                        .getValue(Boolean.class));


                for(int j =0; j< snap.child(v).child("cartItem").getChildrenCount(); j++)
                {
                    order.cartItem.add(snap.child(v)
                            .child("cartItem")
                            .child(String.valueOf(j)).getValue(ListItem.class));
                }
                if(!order.isStatus())
                orderList.add(order);
            }
        } catch (Exception e) {
            System.out.println("List Data Exception   " + snap);
            // makeTextToast("List Data Error");
            e.printStackTrace();
        }
        finally {
            System.out.println(orderList);
            progressDialog.dismiss();
            // if(listItems == null)
            //   makeTextToast("Some Error List Empty");
            makeRecyclerView();
        }
    }


    private void makeRecyclerView()
    {
        for(int i=0; i< orderList.size(); i++)
            orderList.get(i).printAll();
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        if(orderList != null) {
            adapter = new MyOrderAdapter(orderList, getActivity());
            Openable obj = (Openable)getActivity();
            adapter.setOpenable(obj);
            recyclerView.setAdapter(adapter);
        }
    }
}
