package com.marshmallow.beacon.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.Contact;

import java.util.List;
import java.util.Vector;

/**
 * Created by George on 7/13/2018.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder>  {

    private Context context;
    private Vector<String> contactUsernames;

    public ContactsAdapter(Context context, Vector<String> contactUsernames) {
        this.context = context;
        this.contactUsernames = contactUsernames;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_basic, parent, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactHolder holder, final int position) {
        String contactString = contactUsernames.get(position);
        Contact contact = UserManager.getInstance().getContacts().get(contactString);
        holder.contactNameText.setText(contact.getUsername());
        if (contact.getDemandStatus()) {
            holder.demandStatus.setImageResource(R.drawable.beacon_on_mini);
        } else {
            holder.demandStatus.setImageResource(R.drawable.beacon_off_mini);
        }

        if (contact.getSupplyStatus()) {
            holder.supplyStatus.setImageResource(R.drawable.beacon_on_mini);
        } else {
            holder.supplyStatus.setImageResource(R.drawable.beacon_off_mini);
        }

        // TODO handle selecting a single contact
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context.getApplicationContext(), IndividualContactActivity.class);
//                intent.putExtra("contactName", contact);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() { return contactUsernames.size(); }

    public class ContactHolder extends RecyclerView.ViewHolder {
        public TextView contactNameText;
        public ImageView supplyStatus;
        public ImageView demandStatus;

        public ContactHolder(View v) {
            super(v);
            contactNameText = v.findViewById(R.id.contact_name);
            supplyStatus = v.findViewById(R.id.supply_status);
            demandStatus = v.findViewById(R.id.demand_status);
        }
    }
}
