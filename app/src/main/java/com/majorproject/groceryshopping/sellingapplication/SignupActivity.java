package com.majorproject.groceryshopping.sellingapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    // declaration
    private Button buttonSignup;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView viewLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_signup);


        // initialization
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);

        buttonSignup =      (Button)findViewById(R.id.button_signup);
        editTextEmail =     (EditText)findViewById(R.id.editTextSignupEmail);
        editTextPassword =  (EditText)findViewById(R.id.editTextSignupPassword);
        viewLogin =         (TextView)findViewById(R.id.view_Login);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override //check user already logedin
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    //user loged in
                    startActivity(new Intent(SignupActivity.this, MainSellerActivity.class));
                    finish();
                }
                else
                {
                    // user not loged in
                }
            }
        };

        // button on click listner
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignupOnClickListner();
            }
        });
        viewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLoginOnClickListner();
            }
        });
    }

    // open next activity
    private void openNext()
    {
        finish();
        startActivity(new Intent(this, ProfileDetailsActivity.class));

    }

    // viewLogin on click listner
    private void viewLoginOnClickListner()
    {
        //open login
        startActivity(new Intent(this, LoginActivity.class));
    }

    //buttonSignup on click listner
    private void buttonSignupOnClickListner()
    {
        //declaration
        String email, password;

        //initialization
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        // check if entry field are filled or not
        if(TextUtils.isEmpty(email))
        {
            //Email Empty
            Toast.makeText(this, "Email cannot be blank",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            //Password Empty
            Toast.makeText(this, "Password cannot be blank",Toast.LENGTH_SHORT).show();
            return;
        }//both fields are ok to move forward

        //show progress dialog (loading...)
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        // registration
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            //user registration successful
                            Toast.makeText(SignupActivity.this, "User Registered Successfully",Toast.LENGTH_SHORT).show();
                            openNext();
                        }
                        else
                        {
                            //user registration failure
                            Toast.makeText(SignupActivity.this, "User Registration Failure",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    private void goBack()
    {
        finish();
        startActivity(new Intent(this, SelectLoginSignupActivity.class));
    }

    @Override
    public void onBackPressed() {
        goBack();
        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        final Toast t = Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT );
        t.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                t.cancel();

            }
        }, 250);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goBack();
            }
        }, 750);*/
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
