package com.majorproject.groceryshopping.sellingapplication;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileUpdateFragment extends Fragment {

    private SellerDetails sellerDetails = new SellerDetails();

    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextContact;

    private Button buttonSave;

    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public ProfileUpdateFragment() {
        // Required empty public constructor
    }

    public static ProfileUpdateFragment getInstance(SellerDetails details)
    {
        ProfileUpdateFragment fragment = new ProfileUpdateFragment();
        fragment.sellerDetails = details;

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_update, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getActivity());

        editTextName = (EditText) view.findViewById(R.id.editTextUpdateName);
        editTextContact = (EditText) view.findViewById(R.id.editTextUpdateContact);
        editTextAddress = (EditText) view.findViewById(R.id.editTextUpdateAddress);

        buttonSave = (Button) view.findViewById(R.id.button_saveUpdate);

        setComponents();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSaveOnClick();
            }
        });


        return view;
    }

    private void setComponents()
    {
        editTextName.setText(sellerDetails.getSellerName());
        editTextAddress.setText(sellerDetails.getAddress());
        editTextContact.setText(sellerDetails.getContact());
    }

    private boolean checkNumber(String string)
    {
        boolean b = false;

        if(string.length() == 10)
            for(int i=0;i<10;i++) {
                if ((int)string.charAt(i) >= 48 && (int)string.charAt(i) <= 57 && string.charAt(0)!='0')
                    b = true;
                else
                    return false;
            }
        return b;
    }

    private void openNext()
    {
        FragmentMyAccountMainSeller home = FragmentMyAccountMainSeller.getInstance(sellerDetails);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relative_layout_fragment , home)
                .commit();
    }

    private void buttonSaveOnClick()
    {
        String sellerName;
        String contact;
        String address;
        int itemCount;


        sellerName = editTextName.getText().toString().trim();
        contact = editTextContact.getText().toString().trim();
        address = editTextAddress.getText().toString().trim();
        itemCount = sellerDetails.getItemCount();

        progressDialog.setMessage("Saving Data..");
        progressDialog.show();

        if(TextUtils.isEmpty(sellerName))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please enter your Store Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(contact))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please enter your Phone Number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please enter your Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkNumber(contact))
        {
            // check number format
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please enter your 10 digit Mobile Number",Toast.LENGTH_SHORT).show();
            return;
        }

        SellerDetails details = new SellerDetails(sellerName,contact,address,itemCount,firebaseUser.getEmail().toString());
        sellerDetails = details;
        databaseReference.child(getString(R.string.firebase_seller_subclass)).child(firebaseUser.getUid()).setValue(details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                        openNext();
                    }
                });



    }

}

