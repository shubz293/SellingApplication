package com.majorproject.groceryshopping.sellingapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // decleration
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView viewSignup;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_login);

        // initialization
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);

        buttonLogin = (Button)findViewById(R.id.button_login);
        editTextEmail = (EditText)findViewById(R.id.editTextLoginEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextLoginPassword);
        viewSignup = (TextView)findViewById(R.id.view_Signup);

        //check user already logedin

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override //check user already logedin
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    //user loged in
                    openNext();
                } else {
                    // user not loged in
                }
            }
        };

        // on click listners
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOnClickListner();
            }
        });
        viewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupOnClickListner();
            }
        });

    }


    // open next activity
    private void openNext()
    {
        finish();
        startActivity(new Intent(LoginActivity.this, MainSellerActivity.class));
    }

    // signup button on click listner
    private  void signupOnClickListner()
    {
        finish();
        //open signup
        startActivity(new Intent(LoginActivity.this , SignupActivity.class));
    }

    // login button on click listner
    private void loginOnClickListner()
    {
        // declaration
        String email, password;

        // initialization
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        // check if entry field are filled or not
        if(TextUtils.isEmpty(email))
        {
            //Email Empty
            Toast.makeText(LoginActivity.this, "Please enter your Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            //Password Empty
            Toast.makeText(LoginActivity.this, "Please enter your Password",Toast.LENGTH_SHORT).show();
            return;
        }//both fields are ok to move forward

        //show progress dialog (loading...)
        progressDialog.setMessage("Verifying User...");
        progressDialog.show();

        //authenticate username and password
        firebaseAuth.signInWithEmailAndPassword( email , password )
                .addOnCompleteListener(LoginActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // end progressdialog
                                progressDialog.dismiss();

                                // check if login successfull or not
                                if(task.isSuccessful())
                                {
                                    //user login successful
                                    Toast.makeText(LoginActivity.this, "User Login Successfully",Toast.LENGTH_SHORT).show();
                                    //start profile activity
                                    openNext();

                                }
                                else
                                {
                                    //user login failure
                                    Toast.makeText(LoginActivity.this, "User Login Failure",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



    }

    private void goBack()
    {
        finish();
        startActivity(new Intent(LoginActivity.this, SelectLoginSignupActivity.class));
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
                goBack();
            }
        }, 500);*/
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
