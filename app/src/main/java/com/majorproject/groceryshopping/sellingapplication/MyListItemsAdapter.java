package com.majorproject.groceryshopping.sellingapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shubhank Dubey on 31-03-2017.
 */

class MyListItemsAdapter extends RecyclerView.Adapter<MyListItemsAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    public MyListItemsAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.textViewName.setText(listItem.getItemName());
        holder.textViewOffer.setText(listItem.getOffer());
        holder.textViewPrice.setText(listItem.getPrice());
        Picasso.with(context)
                .load(listItem.getImageUrl())
                .fit().centerCrop()
                .into(holder.imageViewImage);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName , textViewPrice, textViewOffer;
        ImageView imageViewImage;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.list_item_name);
            textViewPrice = (TextView) itemView.findViewById(R.id.list_item_price);
            textViewOffer = (TextView) itemView.findViewById(R.id.list_item_offer);
            imageViewImage = (ImageView) itemView.findViewById(R.id.list_item_image_view);

        }
    }
}
