package com.blueeagle.realm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.squareup.leakcanary.LeakCanary;

import Model.Contact;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private ListView listContact;
    private MyAdapter adapter;

    private Realm realm;
    private RealmResults<Contact> contactResults;
    public static RealmConfiguration realmConfiguration;

    private Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        LeakCanary.install(getApplication());

        // Set up Crashlytics, disabled for debug builds
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        Fabric.with(this, new Crashlytics());
        logUser();
        Crashlytics.log(1, "CRASHLYTIC", "Message from craslytic 3........");

        // Find all view id
        findAllViewId();

        // Set listener for view
        setViewListener(toolbar);

        // Create RealmConfiguration
        realmConfiguration = new RealmConfiguration.Builder(this)
                .name("realm.demo")
                .build();

        // create realm object to manager database
        realm = Realm.getInstance(realmConfiguration);

        // get all Contact
        excuteData();

        // add change listener for contactResults
        contactResults.addChangeListener(changeListener);

        adapter = new MyAdapter(this, R.id.list_item_contact, contactResults, true);

        listContact.setAdapter(adapter);
        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact p = adapter.getRealmResults().get(position);
                Intent editContact = new Intent(MainActivity.this, EditContact.class);
                editContact.putExtra(Global.ACTION, Global.ACTION_EDIT);
                editContact.putExtra(Global.ID, p.getId());
                startActivity(editContact);
            }
        });

        // LogBack test
        logger.debug("LogBack - Debug message");
        logger.info("LogBack - Info message");
        logger.warn("LogBack - Warn message");
        logger.error("LogBack - Error message");
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
        Crashlytics.log(1, "CRASHLYTIC", "Message from craslytic 1........");
        Crashlytics.log(1, "CRASHLYTIC", "Message from craslytic 2........");
        Crashlytics.setString("str_id", "String message");
    }


    private void findAllViewId() {
        listContact = (ListView) findViewById(R.id.list_item_contact);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    private void setViewListener(Toolbar toolbar) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditContact.class);
                intent.putExtra(Global.ACTION, Global.ACTION_ADD);
                startActivity(intent);
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    // changeListener will be called when contactResults is changed
    private RealmChangeListener changeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            Toast.makeText(getApplicationContext(), "Data is updated", Toast.LENGTH_SHORT).show();
        }
    };

    private void initData() {
        realm.beginTransaction();
        // create a new contact use copyToRealm method
        Contact per1 = realm.copyToRealm(new Contact("John M.S.H", "0913 211 332", "JohnMSH@gmail.com"));
        Contact per2 = realm.copyToRealm(new Contact("Andrew Andy", "0918 452 635", "AndrewAndy@gmail.com"));
        Contact per3 = realm.copyToRealm(new Contact("Donald Trump", "0123 211 332", "JohnMSH@gmail.com"));
        Contact per4 = realm.copyToRealm(new Contact("Dany Andrew", "01674 211 332", "JohnMSH@gmail.com"));
        Contact per5 = realm.copyToRealm(new Contact("David Beckham", "0989 211 332", "JohnMSH@gmail.com"));
        Contact per6 = realm.copyToRealm(new Contact("Henry Trap", "0986 211 332", "JohnMSH@gmail.com"));
        Contact per7 = realm.copyToRealm(new Contact("Emily Jon", "0967 211 332", "JohnMSH@gmail.com"));
        Contact per8 = realm.copyToRealm(new Contact("Elena Godern", "0968 211 332", "JohnMSH@gmail.com"));
        Contact per9 = realm.copyToRealm(new Contact("Hanan Emilysa", "01687 211 332", "JohnMSH@gmail.com"));
        Contact per10 = realm.copyToRealm(new Contact("Paul Genson", "01231 211 332", "JohnMSH@gmail.com"));
        Contact per11 = realm.copyToRealm(new Contact("Steve Job", "0122 211 332", "JohnMSH@gmail.com"));
        Contact per12 = realm.copyToRealm(new Contact("Taylor Switf", "01689 211 332", "JohnMSH@gmail.com"));
        Contact per13 = realm.copyToRealm(new Contact("Justin Bieer", "0977 211 332", "JohnMSH@gmail.com"));
        Contact per14 = realm.copyToRealm(new Contact("William Henry", "0978 211 332", "JohnMSH@gmail.com"));
        Contact per15 = realm.copyToRealm(new Contact("Victor Sue", "0999 211 332", "JohnMSH@gmail.com"));
        Contact per16 = realm.copyToRealm(new Contact("Linda Hal", "088 211 332", "JohnMSH@gmail.com"));

        // create a new contact use createObject method
        Contact per17 = realm.createObject(Contact.class);
        per17.setFullName("Gothem Mass");
        per17.setPhone("0123 112 562");
        per17.setEmail("GothemMass@gmail.com");

        realm.commitTransaction();

        // LogBack
        logger.debug("LogBack - Init data successful!");
    }

    public void excuteData() {
        // get all contact
        contactResults = realm.where(Contact.class).findAll();

        if (contactResults.size() == 0)
            initData();

        // LogBack
        logger.debug("LogBack - Excute data done!");

        // You can try
        // contactResults = realm.where(Contact.class).findAllAsync(); // findAll Asynchronous
        // Contact c = realm.where(Contact.class).findFirst(); // get first result
        // Contact c = realm.where(Contact.class).findFirstAsync(); // get first result Asynchronous
    }

    private void queriesComplex() {
        contactResults = realm.where(Contact.class)
                .contains("fullName", "n") // fullName contains "n"
                .or() // OR
                .beginGroup()
                .beginsWith("fullName", "T") // fullName begin withs "T"
                // . = AND
                .contains("phone", "332")
                .endGroup()
                .findAll();

        // LogBack
        logger.debug("LogBack - Excute query done!");

        // You can try
        // between(), greaterThan(), lessThan(), greaterThanOrEqualTo() & lessThanOrEqualTo()
        // equalTo() & notEqualTo()
        // contains(), beginsWith() & endsWith()
    }

    private void sortContact() {
        // Sort ascending name
        contactResults = realm.where(Contact.class).findAll();
        contactResults.sort("fullName");
        // or use contactResults.sort("fullName", Sort.ASCENDING);

        // Sort descending name
        // contactResults.sort("fullName", Sort.DESCENDING);
        adapter.notifyDataSetChanged();

        // LogBack
        logger.debug("LogBack - Sort ascending complete!");

        //logUser();
        Crashlytics.setUserEmail("crash@blue.io");
        Crashlytics.log("Log này có được gửi...");
        throw new RuntimeException("Crash message...");
    }

    private void updateListView() {
        adapter.setData(contactResults);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        // avoid leaking memory realm require
        // remove all change listener then close realm

        // remove changeListener of contactResults
        contactResults.removeChangeListeners();
        // close realm
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort:
                sortContact();
                break;

            case R.id.action_queriesComplex:
                queriesComplex();
                break;

            case R.id.action_find_all:
                contactResults = realm.where(Contact.class).findAll();
                break;
        }

        updateListView();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
