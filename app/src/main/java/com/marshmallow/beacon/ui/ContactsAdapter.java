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

import java.util.List;

/**
 * Created by George on 7/13/2018.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder>  {

    private Context context;
    private List<String> contacts;

    public ContactsAdapter(Context context, List<String> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_basic, parent, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactHolder holder, final int position) {
        final String contact = contacts.get(position);
        holder.contactNameText.setText(contact);

        if (contact.equals("GMoney$$") || contact.equals("Dontrella")) {
            holder.supplyStatus.setImageResource(R.drawable.beacon_off_mini);
            holder.demandStatus.setImageResource(R.drawable.beacon_on_mini);
        } else if (contact.equals("JDilla3")) {
            holder.supplyStatus.setImageResource(R.drawable.beacon_on_mini);
            holder.demandStatus.setImageResource(R.drawable.beacon_on_mini);
        } else {
            holder.supplyStatus.setImageResource(R.drawable.beacon_on_mini);
            holder.demandStatus.setImageResource(R.drawable.beacon_off_mini);
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
    public int getItemCount() { return contacts.size(); }

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
