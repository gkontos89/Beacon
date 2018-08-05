package com.marshmallow.beacon.ui.marketing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.backend.BeaconBackend;
import com.marshmallow.beacon.models.marketing.Sponsor;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.SponsorHolder> {

    private Context context;
    private Vector<Sponsor> sponsors;

    public SponsorsAdapter(Context context, Vector<Sponsor> sponsors) {
        this.context = context;
        this.sponsors = sponsors;
    }

    @NonNull
    @Override
    public SponsorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsor_basic, parent, false);
        return new SponsorHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SponsorHolder holder, final int position) {
        final Sponsor sponsor = sponsors.get(position);
        holder.sponsorImage.setImageBitmap(sponsor.getProfilePictureBitmap());
        holder.sponsorName.setText(sponsor.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaconBackend.getInstance().storeSponsorVisit(sponsor);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sponsor.getUrl()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return sponsors.size(); }

    public class SponsorHolder extends RecyclerView.ViewHolder {

        public ImageView sponsorImage;
        public TextView sponsorName;

        public SponsorHolder(View v) {
            super(v);
            sponsorImage = v.findViewById(R.id.sponsor_image);
            sponsorName = v.findViewById(R.id.sponsor_name);
        }
    }
}
