package com.majorproject.groceryshopping.sellingapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainSellerActivity extends AppCompatActivity
        implements Openable,
        NavigationView.OnNavigationItemSelectedListener {

    private TextView navHeaderName;
    private TextView navHeaderEmail;
    private NavigationView navigationView;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private SellerDetails sellerDetails;


    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main_seller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // initilization
        View view = navigationView.getHeaderView(0);
        navHeaderName = (TextView )view.findViewById(R.id.header_name);
        navHeaderEmail = (TextView)view.findViewById(R.id.header_email);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Profile..");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();



        progressDialog.show();
        // check user login state
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override //check user already logedin
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {

                }
                else
                {
                    // user not loged in
                    //start Login/signup activ
                    finish();
                    startActivity(new Intent(MainSellerActivity.this, SelectLoginSignupActivity.class));
                }
            }
        };

        if(sellerDetails == null)
        databaseReference.child(getString(R.string.firebase_seller_subclass)).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateHeader(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*navAccountsClicked();
        navHomeClicked();*/

    }

    private void updateHeader(DataSnapshot dataSnapshot)
    {
        sellerDetails = dataSnapshot.getValue(SellerDetails.class);

        navHeaderName.setText(sellerDetails.getSellerName());
        navHeaderEmail.setText(sellerDetails.getEmail());
/*
        sellerDetails.printAll();
*/

        navHomeClicked();
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_seller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) { navAccountsClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

                if (id == R.id.nav_home) { navHomeClicked();
            // Handle the camera action
        }  else if (id == R.id.nav_items) { navItemsClicked();

        }  else if (id == R.id.nav_accounts) { navAccountsClicked();

        }  else if (id == R.id.nav_logout) { navLogoutClicked();

        }  else if (id == R.id.nav_share) { navShareClicked();

        }  else if (id == R.id.nav_faq) { navFaqClicked();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navShareClicked()
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.share_message);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void navLogoutClicked()
    {
        if(firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
            Toast.makeText(this, "Logout successful",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, SelectLoginSignupActivity.class));
        }
    }

    private void navHomeClicked()
    {
        FragmentHomeMainStore home = FragmentHomeMainStore.getInstance(sellerDetails);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relative_layout_fragment , home).commit();
    }

    private void navFaqClicked()
    {
        FragmentFaqMainSeller faq = new FragmentFaqMainSeller();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relative_layout_fragment , faq).commit();
    }

    private void navAccountsClicked()
    {

        /*System.out.println("from main seller");
        sellerDetails.printAll();*/
        FragmentMyAccountMainSeller account = FragmentMyAccountMainSeller.getInstance(sellerDetails);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.relative_layout_fragment , account)
                .addToBackStack(null)
                .commit();
    }

    private void navItemsClicked()
    {

        FragmentManager manager = getSupportFragmentManager();
        FragmentMyItemsMainSeller item = new FragmentMyItemsMainSeller();
        manager.beginTransaction().replace(R.id.relative_layout_fragment , item).commit();
    }


    @Override
    public void onOrderDelivery(PlacedOrder order) {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference
                .child(getString(R.string.firebase_order_subclass))
                .child(order.getOrderID())
                .child("status")
                .setValue(true);
        makeTextToast("Order Complete");
        navHomeClicked();
        FragmentViewOrders orders = FragmentViewOrders.getInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.relative_layout_fragment, orders)
                .commit();
    }


    private void makeTextToast(String text)
    {
        Toast.makeText(this , text , Toast.LENGTH_SHORT).show();
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
