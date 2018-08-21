package com.marshmallow.beacon.ui.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.marshmallow.beacon.ContactRequestManager;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.contacts.Contact;
import com.marshmallow.beacon.models.contacts.Request;
import com.marshmallow.beacon.models.user.User;
import com.marshmallow.beacon.ui.BaseActivity;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by George on 7/13/2018.
 */
public class ContactsActivity extends BaseActivity {

    // GUI handles
    private CardView incomingRequestsCard;
    private TextView incomingRequestsCountText;
    private CardView outgoingRequestsCard;
    private TextView outgoingRequestsCountText;
    private TextView noContactsText;
    private RecyclerView contactsRecyclerView;
    private FloatingActionButton addContactButton;
    private RecyclerView.LayoutManager contactsLayoutManager;
    private ContactsAdapter contactsAdapter;
    private Vector<Contact> contacts;
    private HashMap<Query, ChildEventListener> contactReferences = null;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;
    private Query requestsInQuery;
    private Query requestsOutQuery;
    private ChildEventListener requestsInQueryListener;
    private ChildEventListener requestsOutQueryListener;

    // Models
    User user;
    ContactRequestManager contactRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contacts);
        activityType = MainActivityTypes.CONTACTS;
        super.onCreate(savedInstanceState);

        // Model initialization
        user = UserManager.getInstance().getUser();
        contactRequestManager = ContactRequestManager.getInstance();

        // GUI handle instantiation
        incomingRequestsCard = findViewById(R.id.incoming_requests);
        incomingRequestsCountText = findViewById(R.id.received_contact_request_count);
        outgoingRequestsCard = findViewById(R.id.outgoing_requests);
        outgoingRequestsCountText = findViewById(R.id.sent_contact_request_count);
        noContactsText = findViewById(R.id.no_contacts_title);
        contacts = new Vector<>() ;
        contactsRecyclerView = findViewById(R.id.contacts_recycler_view);
        contactsLayoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(contactsLayoutManager);
        contactsAdapter = new ContactsAdapter(contacts);
        contactsRecyclerView.setAdapter(contactsAdapter);
        addContactButton = findViewById(R.id.add_contact_fab);

        // Firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();
        initializeContactRequestListeners();
        initializeContactListeners();

        // Set GUI business logic
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewContactActivity.class);
                startActivity(intent);
            }
        });

        incomingRequestsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IncomingContactRequestsActivity.class);
                startActivity(intent);
            }
        });

        outgoingRequestsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OutgoingContactRequestsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeContactRequestListeners() {
        String username = UserManager.getInstance().getUser().getUsername();
        requestsInQuery = firebaseInst.getReference("requests").orderByChild("to").equalTo(username);
        requestsOutQuery = firebaseInst.getReference("requests").orderByChild("from").equalTo(username);

        requestsInQueryListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request requestIn = dataSnapshot.getValue(Request.class);
                contactRequestManager.addRequestIn(requestIn);
                updateRequestCounts();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request requestIn = dataSnapshot.getValue(Request.class);
                contactRequestManager.addRequestIn(requestIn);
                updateRequestCounts();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Request request = dataSnapshot.getValue(Request.class);
                contactRequestManager.removeRequestIn(request);
                updateRequestCounts();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // TODO submit error
            }
        };

        requestsOutQueryListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request requestOut = dataSnapshot.getValue(Request.class);
                contactRequestManager.addRequestOut(requestOut);
                updateRequestCounts();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request requestOut = dataSnapshot.getValue(Request.class);
                contactRequestManager.addRequestOut(requestOut);
                updateRequestCounts();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Request requestOut = dataSnapshot.getValue(Request.class);
                contactRequestManager.removeRequestsOut(requestOut);
                updateRequestCounts();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // submit error
            }
        };

        requestsInQuery.addChildEventListener(requestsInQueryListener);
        requestsOutQuery.addChildEventListener(requestsOutQueryListener);
    }

    public void initializeContactListeners() {
        contactReferences = new HashMap<>();
        if (user.getRolodex() != null) {
            List<String> userNames = user.getRolodex().getUsernames();
            for (String username : userNames) {
                Query query = firebaseInst.getReference("users").orderByChild("username").equalTo(username);
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Contact contact = dataSnapshot.getValue(Contact.class);
                        if (contact != null) {
                            UserManager.getInstance().getContacts().put(contact.getUsername(), contact);

                            contactsAdapter.notifyDataSetChanged();
                            updateNumberOfContacts();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Contact contact = dataSnapshot.getValue(Contact.class);
                        if (contact != null) {
                            UserManager.getInstance().getContacts().put(contact.getUsername(), contact);
                            contactsAdapter.notifyDataSetChanged();
                            updateNumberOfContacts();
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        // TODO removed? Likely just remove from contact list if they deleted profile
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // TODO failures?
                    }
                };

                query.addChildEventListener(childEventListener);
                contactReferences.put(query, childEventListener);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRequestCounts();
    }

    private void updateRequestCounts() {
        incomingRequestsCountText.setText(String.format("%d", contactRequestManager.getIncomingRequests().size()));
        outgoingRequestsCountText.setText(String.format("%d",contactRequestManager.getOutgoingRequests().size()));
    }

    private void updateNumberOfContacts() {
        if (contacts.size() != 0) {
            noContactsText.setVisibility(View.GONE);
            contactsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noContactsText.setVisibility(View.VISIBLE);
            contactsRecyclerView.setVisibility(View.GONE);
        }
    }

    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder>  {

        private Vector<Contact> contacts;

        public ContactsAdapter(Vector<Contact> contacts) {
            this.contacts = contacts;
        }

        @NonNull
        @Override
        public ContactsAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_basic, parent, false);
            return new ContactsAdapter.ContactHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ContactHolder holder, final int position) {
            Contact contact = contacts.get(position);
            holder.contactNameText.setText(contact.getUsername());
            holder.contactProfilePicture.setImageBitmap(contact.getProfilePictureBitmap());
            if (contact.getSignedIn()) {
                holder.signedInStatus.setImageResource(R.drawable.beacon_on_mini);
            } else {
                holder.signedInStatus.setImageResource(R.drawable.beacon_off_mini);
            }

            // TODO handle selecting a single contact
        }

        @Override
        public int getItemCount() { return contacts.size(); }

        public class ContactHolder extends RecyclerView.ViewHolder {
            public ImageView contactProfilePicture;
            public TextView contactNameText;
            public ImageView signedInStatus;

            public ContactHolder(View v) {
                super(v);
                contactProfilePicture = v.findViewById(R.id.contact_profile_picture);
                contactNameText = v.findViewById(R.id.contact_name);
                signedInStatus = v.findViewById(R.id.sign_in_status_image);
            }
        }
    }
}
