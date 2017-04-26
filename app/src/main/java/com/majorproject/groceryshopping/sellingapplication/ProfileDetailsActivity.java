package com.majorproject.groceryshopping.sellingapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileDetailsActivity extends AppCompatActivity {



    private EditText editTextName;
    private EditText editTextContact;
    private EditText editTextAddress;
    private Button buttonSave;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_profile_details);

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextContact = (EditText)findViewById(R.id.editTextContact);
        editTextAddress = (EditText)findViewById(R.id.editTextAddress);
        buttonSave = (Button)findViewById(R.id.button_save);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override //check user already logedin
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    //user loged in

                } else {
                    // user not loged in
                    startActivity(new Intent(ProfileDetailsActivity.this, SelectLoginSignupActivity.class));
                    finish();
                }
            }
        };


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSaveOnCLick();
            }
        });

    }
    private void openNext()
    {
        startActivity(new Intent(this, MainSellerActivity.class));
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


    private void buttonSaveOnCLick()
    {
        String sellerName;
        String contact;
        String address;
        int itemCount;


        sellerName = editTextName.getText().toString().trim();
        contact = editTextContact.getText().toString().trim();
        address = editTextAddress.getText().toString().trim();
        itemCount = 0;

        progressDialog.setMessage("Saving Data..");
        progressDialog.show();

        if(TextUtils.isEmpty(sellerName))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter your Store Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(contact))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter your Phone Number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address))
        {
            //userName Empty
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter your Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkNumber(contact))
        {
            // check number format
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter your 10 digit Mobile Number",Toast.LENGTH_SHORT).show();
            return;
        }

        SellerDetails sellerDetails = new SellerDetails(sellerName,contact,address,itemCount,firebaseUser.getEmail().toString());

        databaseReference.child(getString(R.string.firebase_seller_subclass)).child(firebaseUser.getUid()).setValue(sellerDetails)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(ProfileDetailsActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                openNext();
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
