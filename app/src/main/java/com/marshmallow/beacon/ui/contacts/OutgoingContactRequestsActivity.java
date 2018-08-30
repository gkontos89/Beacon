package com.marshmallow.beacon.ui.contacts;

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
import com.marshmallow.beacon.models.contacts.Request;
import com.marshmallow.beacon.models.user.Rolodex;

import java.util.Vector;

/**
 * Created by George on 7/29/2018.
 */
public class OutgoingContactRequestsActivity extends AppCompatActivity {

    // GUI handles
    private RecyclerView outgoingRequestsRecyclerView;
    private RecyclerView.LayoutManager outgoingRequestsLayoutManager;
    private OutgoingContactRequestsAdapter outgoingContactRequestsAdapter;
    private Vector<Request> outgoingRequests;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;

    // Models
    private ContactRequestManager contactRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_contact_requests);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();

        // Model
        contactRequestManager = ContactRequestManager.getInstance();

        // GUI handle instantiation
        outgoingRequestsRecyclerView = findViewById(R.id.outgoing_request_recycler_view);
        outgoingRequestsLayoutManager = new LinearLayoutManager(this);
        outgoingRequestsRecyclerView.setLayoutManager(outgoingRequestsLayoutManager);
        outgoingRequests = contactRequestManager.getOutgoingRequests();
        outgoingContactRequestsAdapter = new OutgoingContactRequestsAdapter(outgoingRequests);
        outgoingRequestsRecyclerView.setAdapter(outgoingContactRequestsAdapter);
    }

    public class OutgoingContactRequestsAdapter extends RecyclerView.Adapter<OutgoingContactRequestsAdapter.OutgoingContactRequestHolder> {
        private Vector<Request> outgoingRequests;

        public OutgoingContactRequestsAdapter(Vector<Request> outgoingRequests) {
            this.outgoingRequests = outgoingRequests;
        }

        @NonNull
        @Override
        public OutgoingContactRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outgoing_request_basic, parent, false);
            return new OutgoingContactRequestHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final OutgoingContactRequestHolder holder, final int position) {
            final Request outgoingRequest = outgoingRequests.get(position);
            Request.Status status = outgoingRequest.getStatus();
            holder.requestStatus.setText(status.name());
            holder.requestUsername.setText(outgoingRequest.getTo());
            switch(status) {
                case PENDING:
                    holder.requestActionButton.setText(R.string.outgoing_request_action_button_cancel_text);
                    break;
                case ACCEPTED:
                    holder.requestActionButton.setText(R.string.outgoing_request_action_button_confirm_text);
                    break;
                case DECLINED:
                    holder.requestActionButton.setText(R.string.outgoing_request_action_button_clear_text);
                    break;
            }

            holder.requestActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (outgoingRequest.getStatus()) {
                        case PENDING:
                            cancelRequest(outgoingRequest);
                            break;
                        case ACCEPTED:
                            confirmRequest(outgoingRequest);
                            break;
                        case DECLINED:
                            clearRequest(outgoingRequest);
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() { return outgoingRequests.size(); }

        public class OutgoingContactRequestHolder extends RecyclerView.ViewHolder {
            public TextView requestStatus;
            public TextView requestUsername;
            public Button requestActionButton;

            public OutgoingContactRequestHolder(View v) {
                super(v);
                requestStatus = v.findViewById(R.id.request_status_text);
                requestUsername = v.findViewById(R.id.request_username);
                requestActionButton = v.findViewById(R.id.outgoing_request_action_button);
            }
        }
    }

    private void confirmRequest(Request request) {
        DatabaseReference databaseReference = firebaseInst.getReference();
        databaseReference.child("requests").child(request.getUid()).removeValue();
        Rolodex rolodex = UserManager.getInstance().getUser().getRolodex();
        if (rolodex == null) {
            rolodex = new Rolodex();
        }

        rolodex.addUsername(request.getTo());
        if (firebaseAuth.getUid() != null) {
            databaseReference.child("users").child(firebaseAuth.getUid()).child("rolodex").setValue(rolodex);
        }
    }

    private void cancelRequest(Request request) {
        DatabaseReference databaseReference = firebaseInst.getReference();
        databaseReference.child("requests").child(request.getUid()).removeValue();
    }

    private void clearRequest(Request request) {
        cancelRequest(request);
    }
}
