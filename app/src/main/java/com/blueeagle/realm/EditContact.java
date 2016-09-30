package com.blueeagle.realm;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import Model.Contact;
import io.realm.Realm;

public class EditContact extends AppCompatActivity {

    TextView tvContactName;
    EditText etContactName, etPhone, etEmail;
    Button btnSaveContact;

    private Realm realm = Realm.getInstance(MainActivity.realmConfiguration);
    private String Action;
    private int Id;

    private Logger logger = LoggerFactory.getLogger(EditContact.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tvContactName = (TextView) findViewById(R.id.tvContactName);
        etContactName = (EditText) findViewById(R.id.etContactName);
        etPhone = (EditText) findViewById(R.id.etPhoneNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnSaveContact = (Button) findViewById(R.id.btnSaveContact);

        // Indentify action: ADD or EDIT contact
        // Get ACTION by extra of intent that are passed through from a different activity
        final Intent intent = getIntent();
        if (intent.hasExtra(Global.ACTION)) {

            // LogBack
            logger.debug("LogBack - " + intent.getStringExtra(Global.ACTION));

            switch (intent.getStringExtra(Global.ACTION)) {
                case Global.ACTION_ADD:
                    Action = "Add";
                    getSupportActionBar().setTitle("Add New Contact");
                    break;

                case Global.ACTION_EDIT:
                    Id = intent.getIntExtra(Global.ID, 0);
                    fillData(Id);
                    Action = "Edit";
                    getSupportActionBar().setTitle("Update Contact");
                    break;
            }
        }

        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etContactName.getText().toString().equals("") || etPhone.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Contact name and phone is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Action.equals("Add")) {
                    // Add new contact
                    boolean isSuccess = createNewContact();

                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "New Contact is added", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "Can't add new contact", Toast.LENGTH_SHORT).show();
                }

                if (Action.equals("Edit")) {
                    // Update Contact has Id value equal Id
                    boolean isSuccess = updateContact(Id);

                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fillData(int Id) {
        // Get contact that Id attribute value equal to Id
        // findFirst method will return a Contact Object
        Contact c = realm.where(Contact.class).equalTo(Global.ID, Id).findFirst();

        if (c != null) {
            tvContactName.setText(c.getFullName());
            etContactName.setText(c.getFullName());
            etPhone.setText(c.getPhone());
            etEmail.setText(c.getEmail());
        }
    }

    private boolean createNewContact() {

        try {
            realm.beginTransaction();
            // Add new Contact using copyToRealm method
            Contact c = realm.copyToRealm(new Contact(etContactName.getText().toString(),
                    etPhone.getText().toString(),
                    etEmail.getText().toString()));
            realm.commitTransaction();
            return true;

        } catch (Exception e) {
            //Log.d("Add Contact", e.getMessage());
            logger.error("LogBack - Add new contact: " + e);
            return false;
        }
    }

    private boolean updateContact(int id) {

        try {
            realm.beginTransaction();
            Contact c = realm.where(Contact.class).equalTo(Global.ID, Id).findFirst();
            c.setFullName(etContactName.getText().toString()); // Update fullName
            c.setPhone(etPhone.getText().toString()); // Update Phone Numer
            c.setEmail(etEmail.getText().toString()); // Update Email
            realm.commitTransaction();
            return true;

        } catch (Exception e) {
            //Log.d("Update Contact", e.getMessage());
            logger.error("LogBack - Udate contact: " + e);
            return false;
        }
    }

    private void deleteContact() {
        if (Action.equals("Add")) {
            tvContactName.setText("Noname");
            etContactName.setText("");
            etPhone.setText("");
            etEmail.setText("");
        }

        if (Action.equals("Edit")) {
            Contact c = realm.where(Contact.class).equalTo(Global.ID, Id).findFirst();
            realm.beginTransaction();
            // delete contact
            c.removeFromRealm();
            realm.commitTransaction();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.action_delete:
                deleteContact();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
