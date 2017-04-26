package com.majorproject.groceryshopping.sellingapplication;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyItemsMainSeller extends Fragment {



    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    DatabaseReference databaseReference;

    private List<ListItem> listItems = new ArrayList<>();
    private ProgressBar progressBar ;


    public FragmentMyItemsMainSeller() {
        // Required empty public constructor
    }

    public static FragmentMyItemsMainSeller getInstance()
    {
         FragmentMyItemsMainSeller myItemsMainSeller = new FragmentMyItemsMainSeller();
        return myItemsMainSeller;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_items_main_seller, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_my_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = (ProgressBar) view.findViewById(R.id.myItemProgressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);


        getDataSnapshot();


        // Inflate the layout for this fragment
        return view;
    }

    private void setListData(DataSnapshot snap)
    {
        System.out.println(snap);
        try {
//
            for (int i=0; i<= snap.getChildrenCount(); i++)
            {
                String s=String.format("%d",i);
                ListItem item = snap.child(s).getValue(ListItem.class);
                if(item != null) {
                    ListItem listItem = new ListItem();
                    listItem.setSellerName(item.getSellerName());
                    listItem.setOffer(item.getOffer());
                    listItem.setPrice(item.getPrice());
                    listItem.setItemName(item.getItemName());
                    listItem.setImageUrl(item.getImageUrl());
                    listItems.add(listItem);
                }
            }
        } catch (Exception e) {
            System.out.println("List Data Exception   " + snap);
           // makeTextToast("List Data Error");
            e.printStackTrace();
        }
        finally {

                makeRecyclerView();
        }

    }

    private void getDataSnapshot()
    {
        final String SUBCLASS = getString(R.string.firebase_items_subclass);
        databaseReference = FirebaseDatabase.getInstance().getReference();

            System.out.println("read block");
            databaseReference.child(SUBCLASS).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("data block");
                    setListData(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        System.out.println("return block");
    }

    private void makeRecyclerView()
    {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        if(listItems != null) {
            adapter = new MyListItemsAdapter(listItems, getActivity());
            recyclerView.setAdapter(adapter);
        }
    }


}
