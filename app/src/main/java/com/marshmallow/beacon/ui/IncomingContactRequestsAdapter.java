package com.marshmallow.beacon.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.backend.BeaconBackend;
import com.marshmallow.beacon.models.Request;

import java.util.Vector;

/**
 * Created by George on 7/29/2018.
 */
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
    public void onBindViewHolder(@NonNull final IncomingContactRequestHolder holder, final int position) {
        final Request incomingRequest = incomingRequests.get(position);
        holder.requestUsername.setText(incomingRequest.getFrom());

        holder.acceptRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconBackend.getInstance().acceptRequest(incomingRequest);
            }
        });

        holder.declineRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconBackend.getInstance().declineRequest(incomingRequest);
            }
        });
    }

    @Override
    public int getItemCount() { return incomingRequests.size(); }

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
