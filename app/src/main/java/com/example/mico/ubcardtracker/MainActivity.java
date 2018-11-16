package com.example.mico.ubcardtracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mico.ubcardtracker.m_UI.ScannedCardCustomAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView UserName, UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentHome fragmentHome = new FragmentHome();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragmentHome).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header_view = navigationView.getHeaderView(0);

        UserName = header_view.findViewById(R.id.et_username);
        UserEmail = header_view.findViewById(R.id.et_email);

        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"null");
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"null");

        UserName.setText(name);
        UserEmail.setText(email);


    }
    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure?");
        alertDialogBuilder.setMessage("You want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.putString(Config.ID_SHARED_PREF, "");
                        editor.putString(Config.NAME_SHARED_PREF, "");
                        editor.putString(Config.EMAIL_SHARED_PREF, "");
                        editor.putString(Config.ROLE_SHARED_PREF, "");
                        editor.putString(Config.COMPANY_SHARED_PREF, "");
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(MainActivity.this);
            myAlertDialog.setTitle("Are you sure?");
            myAlertDialog.setMessage("Do you want to exit application?");
            myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }});
            myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    // do nothing....
                }});
            myAlertDialog.show();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home){
            FragmentHome fragmentHome = new FragmentHome();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragmentHome).commit();
        } else if(id == R.id.nav_all) {
            FragmentAllCards fragmentAllCards = new FragmentAllCards();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragmentAllCards).commit();
        } else if (id == R.id.nav_scanned) {
            FragmentScanned fragmentScanned = new FragmentScanned();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragmentScanned).commit();
        } else if (id == R.id.nav_delivered) {
            FragmentDelivered fragmentDelivered = new FragmentDelivered();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragmentDelivered).commit();
        } else if (id == R.id.nav_undelivered) {
            FragmentUndelivered fragmentUndelivered = new FragmentUndelivered();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragmentUndelivered).commit();
        } else if (id == R.id.nav_on_delivery) {
            FragmentOnDelivery fragmentOnDelivery = new FragmentOnDelivery();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragmentOnDelivery).commit();
        } else if (id == R.id.nav_purging) {
            FragmentPurging fragmentPurging = new FragmentPurging();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragmentPurging).commit();
        }else if(id==R.id.nav_share){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        ScannedCardCustomAdapter.compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ScannedCardCustomAdapter.compositeDisposable.clear();
        super.onDestroy();
    }
}
