package com.marshmallow.beacon;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Vector;

/**
 * Created by George on 7/13/2018.
 */
public class ContactsActivity extends BaseActivity {

    // GUI handles
    private RecyclerView contactsRecyclerView;
    private RecyclerView.LayoutManager contactsLayoutManager;
    private ContactsAdapter contactsAdapter;
    private List<String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contacts);
        super.onCreate(savedInstanceState);


        // Create dummy users
        contacts = new Vector<>();
        contacts.add("GMoney$$");
        contacts.add("Dontrella");
        contacts.add("JDilla3");
        contacts.add("SuperSix2");
        contacts.add("Dad");
        contacts.add("BDo4Life");
        contacts.add("T-Pain");
        contacts.add("ElChapo");



        // GUI handle instantiation
        contactsRecyclerView = findViewById(R.id.contacts_recycler_view);
        contactsLayoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(contactsLayoutManager);
        contactsAdapter = new ContactsAdapter(this, contacts);
        contactsRecyclerView.setAdapter(contactsAdapter);
    }
}
