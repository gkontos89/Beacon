package com.marshmallow.beacon.ui.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marshmallow.beacon.ContactRequestManager;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.broadcasts.RequestUpdateBroadcast;
import com.marshmallow.beacon.models.contacts.Request;
import com.marshmallow.beacon.models.user.Rolodex;

import java.util.Vector;

/**
 * Created by George on 7/29/2018.
 */
public class IncomingContactRequestsActivity extends AppCompatActivity {

    // GUI handles
    private RecyclerView incomingRequestsRecyclerView;
    private RecyclerView.LayoutManager incomingRequestsLayoutManager;
    private IncomingContactRequestsAdapter incomingContactRequestsAdapter;
    private Vector<Request> incomingRequests;

    // Models
    private ContactRequestManager contactRequestManager;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_contact_requests);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();

        // Model
        contactRequestManager = ContactRequestManager.getInstance();

        // GUI handle instantiation
        incomingRequestsRecyclerView = findViewById(R.id.incoming_requests_recycler_view);
        incomingRequestsLayoutManager = new LinearLayoutManager(this);
        incomingRequestsRecyclerView.setLayoutManager(incomingRequestsLayoutManager);
        incomingRequests = contactRequestManager.getIncomingRequests();
        incomingContactRequestsAdapter = new IncomingContactRequestsAdapter(incomingRequests);
        incomingRequestsRecyclerView.setAdapter(incomingContactRequestsAdapter);
    }

    public class IncomingContactRequestsAdapter extends RecyclerView.Adapter<IncomingContactRequestsAdapter.IncomingContactRequestHolder> {
        private Vector<Request> incomingRequests;

        public IncomingContactRequestsAdapter(Vector<Request> incomingRequests) {
            this.incomingRequests = incomingRequests;
        }

        @NonNull
        @Override
        public IncomingContactRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.incoming_request_basic, parent, false);
            return new IncomingContactRequestHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull IncomingContactRequestHolder holder, int position) {
            final Request incomingRequest = incomingRequests.get(position);
            holder.requestUsername.setText(incomingRequest.getFrom());
            holder.acceptRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptRequest(incomingRequest);
                }
            });

            holder.declineRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    declineRequest(incomingRequest);
                }
            });
        }

        @Override
        public int getItemCount() {
            return incomingRequests.size();
        }

        public class IncomingContactRequestHolder extends RecyclerView.ViewHolder {
            public Button acceptRequestButton;
            public TextView requestUsername;
            public Button declineRequestButton;

            public IncomingContactRequestHolder(View v) {
                super(v);
                acceptRequestButton = v.findViewById(R.id.accept_request_action_button);
                requestUsername = v.findViewById(R.id.request_username);
                declineRequestButton = v.findViewById(R.id.decline_request_action_button);
            }
        }
    }

    public void acceptRequest(Request request) {
        DatabaseReference databaseReference = firebaseInst.getReference();
        request.setStatus(Request.Status.ACCEPTED);
        databaseReference.child("requests").child(request.getUid()).setValue(request);
        Rolodex rolodex = UserManager.getInstance().getUser().getRolodex();
        if (rolodex == null) {
            rolodex = new Rolodex();
        }

        rolodex.addUsername(request.getFrom());
        if (firebaseAuth.getUid() != null) {
            databaseReference.child("users").child(firebaseAuth.getUid()).child("rolodex").setValue(rolodex);
        }
    }

    public void declineRequest(Request request) {
        DatabaseReference databaseReference = firebaseInst.getReference();
        request.setStatus(Request.Status.DECLINED);
        databaseReference.child("requests").child(request.getUid()).setValue(request);
    }
}
