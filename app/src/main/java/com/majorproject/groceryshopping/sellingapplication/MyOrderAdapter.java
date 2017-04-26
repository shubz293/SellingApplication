package com.majorproject.groceryshopping.sellingapplication;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubhank Dubey on 07-04-2017.
 */

class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>{

    private List<PlacedOrder> placedOrders = new ArrayList<>();
    private Context context;

    private Openable openable;


    public MyOrderAdapter(List<PlacedOrder> placedOrders, Context context) {
        this.placedOrders = placedOrders;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.placed_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlacedOrder placedOrder = new PlacedOrder();
        try {
            placedOrder = placedOrders.get(position);

            holder.textViewName.setText(placedOrder.getCustomerName());
            holder.textViewAddress.setText(placedOrder.getCustomerAddress());
            String count = String.valueOf(placedOrder.getCartItemSize());
            holder.textViewCount.setText(count);
            String price = String.valueOf(placedOrder.getTotalAmount());
            holder.textViewPrice.setText(price);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace(System.err);
        }

        final PlacedOrder finalPlacedOrder = placedOrder;
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemOnClick(finalPlacedOrder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return placedOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewName, textViewAddress, textViewPrice, textViewCount;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.card_view_name);
            textViewAddress = (TextView) itemView.findViewById(R.id.card_view_address);
            textViewCount = (TextView) itemView.findViewById(R.id.card_view_count);
            textViewPrice = (TextView) itemView.findViewById(R.id.card_view_price);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.placed_order_linear_layout);

        }
    }

    private void listItemOnClick(final PlacedOrder placedOrder)
    {
        View mView = LayoutInflater.from(context).inflate(R.layout.selected_order_view, null) ;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.setView(mView).create();
        alertDialog.show();

        TextView textViewName = (TextView) mView.findViewById(R.id.view_order_name);
        TextView textViewAddress = (TextView) mView.findViewById(R.id.view_order_address);
        TextView textViewCount = (TextView) mView.findViewById(R.id.view_order_total);
        TextView textViewPrice = (TextView) mView.findViewById(R.id.view_order_price);

        List<ListItem> listItems = placedOrder.getCartItem();
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.view_order_recycler_view);
        RecyclerView.Adapter adapter = new MyListItemsAdapter(listItems , context);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        textViewName.setText(placedOrder.getCustomerName());
        textViewAddress.setText(placedOrder.getCustomerAddress());
        String count = String.valueOf(placedOrder.getCartItemSize());
        textViewCount.setText(String.format("Total %s items", count));
        String price = String.valueOf(placedOrder.getTotalAmount());
        textViewPrice.setText(String.format("Rs. %s", price));

        recyclerView.setAdapter(adapter);




        Button backButton = (Button) mView.findViewById(R.id.view_order_back_button);
        final Button completeButton = (Button) mView.findViewById(R.id.view_order_complete_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                completeButtonOnClick();
                getOpenable().onOrderDelivery(placedOrder);
                alertDialog.dismiss();
            }
        });

    }
/*
    private void completeButtonOnClick()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getInstance();

    }*/

    public Openable getOpenable() {
        return openable;
    }

    public void setOpenable(Openable openable) {
        this.openable = openable;
    }
}
