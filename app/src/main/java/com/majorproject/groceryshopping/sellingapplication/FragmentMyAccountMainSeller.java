package com.majorproject.groceryshopping.sellingapplication;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyAccountMainSeller extends Fragment {


    private View mView;
    private AlertDialog alertDialog;

    private TextView textViewMyAccountName;
    private TextView textViewMyAccountContact;
    private TextView textViewMyAccountAddress;
    private TextView textViewMyAccountEmail;

    private Button buttonUpdateProfile;
    private Button buttonChangePassword;

    private SellerDetails sellerDetails = new SellerDetails();

    public FragmentMyAccountMainSeller() {
       /* System.out.println("from constructor");*/
        // Required empty public constructor
    }

    public static FragmentMyAccountMainSeller getInstance(SellerDetails details)
    {

        FragmentMyAccountMainSeller myAccountMainSeller = new FragmentMyAccountMainSeller();
        myAccountMainSeller.sellerDetails = details;
        return myAccountMainSeller;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account_main_seller, container, false);
        mView = inflater.inflate(R.layout.dialog_change_password, null);
        textViewMyAccountName = (TextView) view.findViewById(R.id.my_account_name);
        textViewMyAccountAddress = (TextView) view.findViewById(R.id.my_account_address);
        textViewMyAccountContact = (TextView) view.findViewById(R.id.my_account_contact);
        textViewMyAccountEmail = (TextView) view.findViewById(R.id.my_account_email);

        buttonUpdateProfile = (Button) view.findViewById(R.id.my_account_button_update_profile);
        buttonChangePassword = (Button) view.findViewById(R.id.my_account_button_change_password);


        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUpdateProfileOnClick();
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChangePasswordOnClick();
            }
        });

        System.out.println("Working Fine");
        setComponents();
        return view;
    }


    public void setComponents()
    {
       /* System.out.println("from my account fragment");
        sellerDetails.printAll();*/
        textViewMyAccountEmail.setText(sellerDetails.getEmail());
        textViewMyAccountContact.setText(sellerDetails.getContact());
        textViewMyAccountAddress.setText(sellerDetails.getAddress());
        textViewMyAccountName.setText(sellerDetails.getSellerName());
    }

    private void buttonUpdateProfileOnClick()
    {
        ProfileUpdateFragment account = ProfileUpdateFragment.getInstance(sellerDetails);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.relative_layout_fragment, account)
                .addToBackStack(null)
                .commit();
    }

    private void buttonChangePasswordOnClick()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

        //mView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);

        final Button buttonUpdatePassword = (Button) mView.findViewById(R.id.save_button_change_password);


        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUpdatePasswordOnClick();
            }
        });

        mBuilder.setView(mView);
        alertDialog = mBuilder.create();
        alertDialog.show();

    }//end

    private void buttonUpdatePasswordOnClick()
    {
        final String oldpass, newpass1, newpass2;

        final EditText oldPassword  = (EditText) mView.findViewById(R.id.old_pass);
        final EditText newPassword1 = (EditText) mView.findViewById(R.id.new_pass1);
        final EditText newPassword2 = (EditText) mView.findViewById(R.id.new_pass2);

        oldpass = oldPassword.getText().toString().trim();
        newpass1 = newPassword1.getText().toString().trim();
        newpass2 = newPassword2.getText().toString().trim();

        System.out.println(oldpass +" "+newpass1+" "+newpass2);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            makeTextToast("Not logged in, Please login again");
            getActivity().finish();
            startActivity(new Intent(getActivity(), SelectLoginSignupActivity.class));
        }

        if(TextUtils.isEmpty(oldpass))
        {
            makeTextToast("Enter your old Password");
            return;
        }
        if(TextUtils.isEmpty(newpass1))
        {
            Toast.makeText(getActivity(),"Enter your new Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(newpass2))
        {
            makeTextToast("Enter your new Password");
            return;
        }
        if(!TextUtils.equals(newpass1,newpass2))
        {
            makeTextToast("New Password not same");
            return;
        }
        else if(TextUtils.equals(oldpass,newpass1))
        {
            makeTextToast("New Password cannot be same as old password");
            return;
        }


        final String email = user.getEmail();
        AuthCredential credential;
        credential = EmailAuthProvider.getCredential(email,oldpass);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updatePassword(newpass1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(!task.isSuccessful()){
                                                makeTextToast("Something went wrong. Please try again later");
                                                oldPassword.setText("");
                                                newPassword1.setText("");
                                                newPassword2.setText("");

                                            }else {
                                                makeTextToast("Password Successfully Modified");
                                                hideAlertDialog();
                                            }
                                        }
                                    });
                        }else {
                            makeTextToast("Authentication Failed");
                            oldPassword.setText("");
                            newPassword1.setText("");
                            newPassword2.setText("");
                        }
                    }
                });
    }


    private void hideAlertDialog()
    {
        alertDialog.dismiss();
    }

    private void makeTextToast(String text)
    {
        System.out.println("  "+ text);
        Toast.makeText(getActivity(), text , Toast.LENGTH_SHORT).show();
    }

}