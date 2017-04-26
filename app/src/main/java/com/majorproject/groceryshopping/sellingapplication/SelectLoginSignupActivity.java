package com.majorproject.groceryshopping.sellingapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SelectLoginSignupActivity extends AppCompatActivity {

    private Button login;
    private Button signup;
    private static boolean doubleBackToExitPressedOnce = false;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_select_login_signup);
        //initialization
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);

        // check if user already loged in
        firebaseAuth = FirebaseAuth.getInstance();

        // check if user already loged in
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override //check user already logedin
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    //user loged in
                    startActivity(new Intent(SelectLoginSignupActivity.this, MainSellerActivity.class));
                    finish();
                }
                else
                {
                    // user not loged in
                }
            }
        };

        // onclick listner for buttons
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOnClickListner();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupOnClickListner();
            }
        });
    }

    //
    private void loginOnClickListner()
    {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void signupOnClickListner()
    {
        finish();
        startActivity(new Intent(this, SignupActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            doubleBackToExitPressedOnce = false;
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT ).show();

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
