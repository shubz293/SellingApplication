package com.majorproject.groceryshopping.sellingapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemFragment extends Fragment {

private static final int GALLERY_INTENT = 2;
    private static boolean DATA_UPLOADED = false;
    private static boolean BUTTON_CLICKED = false;

    private EditText editTextItemName;
    private EditText editTextItemMarketPrice;
    private EditText editTextItemOffer;

    private Button buttonSaveItem;
    private ImageView imageViewAddImage;
    private SellerDetails sellerDetails = new SellerDetails();
    ProgressDialog progressDialog ;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;


    private Long itemID = 0L;
    private Uri uri ;
    private ListItem item;

    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment getInstance(SellerDetails details)
    {
        AddItemFragment addItemFragment = new AddItemFragment();
        addItemFragment.setSellerDetails(details);

        return addItemFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        Uri uri = Uri.parse("android.resource://com.majorproject.groceryshopping.sellingapplication/drawable/no_image_available.png");

        item = new ListItem();
        storageReference.child(getString(R.string.firebase_itemImage_subclass));

        //Readitemid
        databaseReference.child(getString(R.string.firebase_itemID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemID = (Long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        editTextItemName = (EditText) view.findViewById(R.id.addItemName_addItem);
        editTextItemMarketPrice = (EditText) view.findViewById(R.id.addMarketPrice_addItem);
        editTextItemOffer = (EditText) view.findViewById(R.id.addOffer_addItem);
        progressDialog = new ProgressDialog(getActivity());

        buttonSaveItem = (Button) view.findViewById(R.id.saveItem_addItem);
        imageViewAddImage = (ImageView) view.findViewById(R.id.addItemImage);


        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BUTTON_CLICKED = true;
                imageViewAddImageOnClick();

            }
        });

        buttonSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BUTTON_CLICKED) {
                    progressDialog.setMessage("Saving Data");
                    progressDialog.show();
                    getImageUrl();
                    buttonSaveItemOnClick();
                }
                else
                {
                    makeTextToast("Please Attach an Image");
                }
            }
        });



        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT) {
            if (resultCode == RESULT_OK) {
                Uri u = data.getData();
                Picasso.with(getActivity()).load(u).centerCrop().fit().into(imageViewAddImage);
                setUri(u);

            }
            else //if (requestCode == RESULT_CANCELED)
            {
                BUTTON_CLICKED = false;
            }
        }
    }



    private void imageViewAddImageOnClick()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    private void buttonSaveItemOnClick()
    {

        String itemName, stringMarketPrice, stringOffer;
        itemName = editTextItemName.getText().toString().trim();
        stringMarketPrice = editTextItemMarketPrice.getText().toString().trim();
        stringOffer = editTextItemOffer.getText().toString().trim();
        if(TextUtils.isEmpty(itemName) || TextUtils.isEmpty(stringMarketPrice) )
        {
            makeTextToast("Please fill all the details");
            return;
        }
        if(TextUtils.isEmpty(stringOffer))
            stringOffer = "0";

        item.setItemName(itemName);
        item.setPrice(getPriceString(stringOffer, stringMarketPrice));
        item.setOffer(getOfferString(stringOffer, stringMarketPrice));
        item.setSellerName(sellerDetails.getSellerName());

        if(!DATA_UPLOADED)
        {
            if(!item.checkEmpty())
            {
                uploadData(item);
                DATA_UPLOADED = true ;
            }
            else
            {
                DATA_UPLOADED = false;
            }
        }

       /*
       ListItem item = new ListItem(
       itemName,
       getPriceString(stringOffer, stringMarketPrice),
       getOfferString(stringOffer, stringMarketPrice),
       sellerDetails.getSellerName(),
       getImageUrl());
        */

    }




    private void getImageUrl()
    {
        StorageReference childRef = storageReference.child(getString(R.string.firebase_itemImage_subclass)).child(String.valueOf(itemID));
       if(getUri()!= null) {
           UploadTask uploadTask = childRef.putFile(getUri());
           uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @SuppressWarnings("VisibleForTests")
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                   makeTextToast("Image Upload Successful");
                   uri = taskSnapshot.getDownloadUrl();
                   item.setImageUrl(uri.toString());
                   if (!DATA_UPLOADED) {
                       if (!item.checkEmpty()) {
                           uploadData(item);
                           DATA_UPLOADED = true;
                       }
                       else {
                           DATA_UPLOADED = false;
                       }
                   }
                   progressDialog.dismiss();


               }
           });
       }
       else
       {
            System.out.println(" uri null "+ getUri());
       }

    }

    private void uploadData(ListItem item)
    {
        databaseReference.child(getString(R.string.firebase_items_subclass)).child(itemID.toString()).setValue(item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            makeTextToast("Item Saved Successfully");
                        }
                        else
                        {
                            makeTextToast("Data Upload Error");
                        }
                    }
                });
        itemID++;
        databaseReference.child(getString(R.string.firebase_itemID)).setValue(itemID);
        DATA_UPLOADED = false;
    }





    private String getPriceString(String offer , String marketPrice)
    {
        int o,mp;
        o= Integer.parseInt(offer);
        mp= Integer.parseInt(marketPrice);
        mp = mp - (mp*o)/100 ;
        return String.format(" Rs. %d. ",mp);
    }

    private String getOfferString(String offer , String marketPrice)
    {
        int o,mp;
        o= Integer.parseInt(offer);
        mp= Integer.parseInt(marketPrice);
        if(o==0)
            return "No Offer";
        else
            return String.format(" %d %% discount on Rs.%d",o,mp);
    }





    private void makeTextToast(String text)
    {
        Toast.makeText(getActivity(), text , Toast.LENGTH_SHORT).show();
    }

    public Uri getUri()
    {
        return uri;
    }
    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setSellerDetails(SellerDetails sellerDetails)
    {
        this.sellerDetails = sellerDetails;
    }

}