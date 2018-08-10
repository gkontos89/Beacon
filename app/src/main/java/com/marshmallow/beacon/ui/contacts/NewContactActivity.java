package com.marshmallow.beacon.ui.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.broadcasts.AddNewContactBroadcast;
import com.marshmallow.beacon.models.contacts.Request;

/**
 * Created by George on 7/28/2018.
 */
public class NewContactActivity extends AppCompatActivity {

    // GUI handles
    private EditText contactUserNameEditText;
    private Button addContactButton;
    private LinearLayout progressBarLinearLayout;
    private TextView progressBarText;

    // Firebase
    private FirebaseDatabase firebaseInst;

    // Username text
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        // Firebase
        firebaseInst = FirebaseDatabase.getInstance();

        // Set GUI handles
        contactUserNameEditText = findViewById(R.id.new_contact_username_edit_text);
        addContactButton = findViewById(R.id.add_contact_button);
        progressBarLinearLayout = findViewById(R.id.new_contact_progress_bar_layout);
        progressBarText = findViewById(R.id.new_contact_progress_bar_text);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEntry()) {
                    if (notAlreadyAContact() && notSearchingThyself()) {
                        showProgressBar("Submitting contact request...");
                        username = contactUserNameEditText.getText().toString();
                        final Request request = new Request(username, UserManager.getInstance().getUser().getUsername());
                        firebaseInst.getReference().child("users").orderByChild("username").equalTo(request.getTo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // If user exists submit the request!
                                if (dataSnapshot.exists()) {
                                    // Create Request model and submit to database
                                    DatabaseReference requestsReference = firebaseInst.getReference("requests");
                                    String requestUniqueId = requestsReference.child("requests").push().getKey();
                                    if (requestUniqueId != null) {
                                        request.setUid(requestUniqueId);
                                        requestsReference.child(requestUniqueId).setValue(request);
                                        contactRequestSubmitSucceeded();
                                    } else {
                                        requestCreationFailed("Unable to create new request");
                                    }
                                } else {
                                    contactRequestContactNotFound();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                contactRequestSubmitFailed(databaseError.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    private boolean notSearchingThyself() {
        String requestedUsername = contactUserNameEditText.getText().toString();
        if (requestedUsername.equals(UserManager.getInstance().getUser().getUsername())) {
            Toast.makeText(this, "You cannot add yourself!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean notAlreadyAContact() {
        String requestedUsername = contactUserNameEditText.getText().toString();
        if (UserManager.getInstance().getUser().getRolodex() != null &&
                UserManager.getInstance().getUser().getRolodex().getUsernames().contains(requestedUsername)) {
            Toast.makeText(this, "You are already connected to " + requestedUsername, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void requestCreationFailed(String failureString) {
        hideProgressBar();
        Toast.makeText(this, failureString, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEntry() {
        if (contactUserNameEditText.toString().isEmpty()) {
            contactUserNameEditText.setError("username can't be empty");
            return false;
        } else {
            return true;
        }
    }

    private void contactRequestContactNotFound() {
        hideProgressBar();
        Toast.makeText(this, username + " is not an existing user!", Toast.LENGTH_SHORT).show();
    }

    private void contactRequestSubmitFailed(String failureString) {
        hideProgressBar();
        Toast.makeText(this, "Contact Request Failed: " + failureString, Toast.LENGTH_SHORT).show();
    }

    private void contactRequestSubmitSucceeded() {
        Toast.makeText(this, "Contact Request Submitted!", Toast.LENGTH_SHORT).show();
        progressBarLinearLayout.setVisibility(View.GONE);
        SystemClock.sleep(300);
        finish();
    }

    public void showProgressBar(String progressBarText) {
        // Hide all views in this layout and only show the progress bar linear layout
        findViewById(R.id.contact_username_title).setVisibility(View.INVISIBLE);
        contactUserNameEditText.setVisibility(View.INVISIBLE);
        addContactButton.setVisibility(View.INVISIBLE);

        this.progressBarText.setText(progressBarText);
        progressBarLinearLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        findViewById(R.id.contact_username_title).setVisibility(View.VISIBLE);
        contactUserNameEditText.setVisibility(View.VISIBLE);
        addContactButton.setVisibility(View.VISIBLE);

        // Hide progress bar
        progressBarLinearLayout.setVisibility(View.GONE);
    }
}
