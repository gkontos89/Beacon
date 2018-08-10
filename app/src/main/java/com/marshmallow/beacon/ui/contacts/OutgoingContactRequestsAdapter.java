package com.marshmallow.beacon.ui.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.models.contacts.Request;

import java.util.Vector;

/**
 * Created by George on 7/29/2018.
 */
public class OutgoingContactRequestsAdapter extends RecyclerView.Adapter<OutgoingContactRequestsAdapter.OutgoingContactRequestHolder> {
    private Vector<Request> outgoingRequests;

    public OutgoingContactRequestsAdapter(Vector<Request> outgoingRequests) {
        this.outgoingRequests = outgoingRequests;
    }

    public void setOutgoingRequestReference(Vector<Request> outgoingRequests) {
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
                        BeaconBackend.getInstance().cancelRequest(outgoingRequest);
                        break;
                    case ACCEPTED:
                        BeaconBackend.getInstance().confirmRequest(outgoingRequest);
                        break;
                    case DECLINED:
                        BeaconBackend.getInstance().clearRequest(outgoingRequest);
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
