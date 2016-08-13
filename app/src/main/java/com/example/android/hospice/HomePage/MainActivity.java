package com.example.android.hospice.HomePage;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hospice.MapsActivity;
import com.example.android.hospice.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , GoogleApiClient.OnConnectionFailedListener{
    DrawerLayout drawer;
    private BottomBar mBottomBar;
    public String personName;
    public String personEmail;
    public String personId;
    public Uri personPhoto;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_main);
        userProfileInformation(intent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
	    mBottomBar.useFixedMode();
	    mBottomBar.setActiveTabColor("#cc0000");
        View view = getLayoutInflater().inflate(R.layout.home_nav_header_main, null);
        TextView name = (TextView) view.findViewById(R.id.personName);
        name.setText(personName);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottom_nav_home) {
                    Home home = new Home();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,home)
                            .commit();
                }
                if (menuItemId == R.id.bottom_nav_store) {
                    MediStore mediStore = new MediStore();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,mediStore)
                            .commit();
                }
                if (menuItemId == R.id.bottom_nav_health_status) {
                    FitnessActivity mediStatus = new FitnessActivity();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,mediStatus)
                            .commit();
                }
                if (menuItemId == R.id.bottom_nav_community) {
                    Community community = new Community();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,community)
                            .commit();
                }
            }


            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

	    mBottomBar.selectTabAtPosition(0, false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
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
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(),"Search", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Toast.makeText(MainActivity.this, "Signed Out.", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intentHome = new Intent(this, com.example.android.hospice.MainActivity.class);
            startActivity(intentHome);

        }
        else if (id == R.id.nearby_stores) {
            // Handle the camera action
        }
        else if (id == R.id.nearby_emergency) {
            Intent intentEmergency = new Intent(this, MapsActivity.class);
            startActivity(intentEmergency);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void userProfileInformation(Intent intent){


        personName = intent.getStringExtra("personName");
        personEmail = intent.getStringExtra("personEmail");
        personId = intent.getStringExtra("personId");
        personPhoto  = intent.getParcelableExtra("personPhoto");


    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("TAG: ", "onConnectionFailed" + connectionResult);
    }
}
