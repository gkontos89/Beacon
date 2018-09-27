package com.marshmallow.beacon.ui.marketing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.MarketingManager;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.marketing.Sponsor;
import com.marshmallow.beacon.models.marketing.SponsorMarketValues;
import com.marshmallow.beacon.models.marketing.SponsorVisitEvent;
import com.marshmallow.beacon.models.marketing.UserMarketDataSnapshot;
import com.marshmallow.beacon.models.user.User;
import com.marshmallow.beacon.ui.BaseActivity;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SponsorsActivity extends BaseActivity {

    // GUI handles
    private RecyclerView sponsorsRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SponsorsAdapter sponsorsAdapter;
    private Vector<Sponsor> sponsors;

    // Firebase
    private FirebaseDatabase firebaseInst;
    private DatabaseReference sponsorsReference;
    private ChildEventListener sponsorChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sponsors);
        activityType = MainActivityTypes.SPONSORS;
        super.onCreate(savedInstanceState);

        firebaseInst = FirebaseDatabase.getInstance();

        sponsors = new Vector<>();
        sponsorsRecyclerView = findViewById(R.id.sponsors_recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        sponsorsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        sponsorsAdapter = new SponsorsAdapter(sponsors);
        sponsorsRecyclerView.setAdapter(sponsorsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeSponsorListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        destroySponsorListeners();
    }

    private void initializeSponsorListeners() {
        sponsorsReference = firebaseInst.getReference("sponsors");
        sponsorsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sponsorChildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Sponsor sponsor = dataSnapshot.getValue(Sponsor.class);
                        sponsors.add(sponsor);
                        sponsorsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        // TODO handle this.
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                sponsorsReference.addChildEventListener(sponsorChildEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void destroySponsorListeners() {
        sponsorsReference.removeEventListener(sponsorChildEventListener);
        sponsorChildEventListener = null;
        sponsorsReference = null;
        sponsors.clear();
    }

    public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.SponsorHolder> {

        private Vector<Sponsor> sponsors;

        public SponsorsAdapter(Vector<Sponsor> sponsors) {
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
            holder.setSponsor(sponsors.get(position));
            // TODO handle sponsor image from database.  For now it is hardcoded in App
            //holder.sponsorImage.setImageBitmap(sponsor.getProfilePictureBitmap());
            holder.sponsorName.setText(holder.getSponsor().getName());
            switch (holder.getSponsor().getName()) {
                case "At Work Sports Bar":
                    holder.sponsorImage.setImageResource(R.drawable.at_work_sports_bar_and_grill);
                    holder.setSponsorImageResId(R.drawable.at_work_sports_bar_and_grill);
                    break;

                case "Brick and Motor Botique":
                    holder.sponsorImage.setImageResource(R.drawable.brick_and_motor_botique);
                    holder.setSponsorImageResId(R.drawable.brick_and_motor_botique);
                    break;

                case "A Jacq of All Trades":
                    holder.sponsorImage.setImageResource(R.drawable.jacq_of_all_trades);
                    holder.setSponsorImageResId(R.drawable.jacq_of_all_trades);
                    break;

                case "Make It Train":
                    holder.sponsorImage.setImageResource(R.drawable.make_it_train);
                    holder.setSponsorImageResId(R.drawable.make_it_train);
                    break;

                case "Rhodes Capital Management":
                    holder.sponsorImage.setImageResource(R.drawable.rhodes_capital_management);
                    holder.setSponsorImageResId(R.drawable.rhodes_capital_management);
                    break;
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), IndividualSponsorActivity.class);
                    intent.putExtra(IndividualSponsorActivity.sponsorUidKey, holder.getSponsor().getUid());
                    intent.putExtra(IndividualSponsorActivity.sponsorNameKey, holder.getSponsor().getName());
                    intent.putExtra(IndividualSponsorActivity.sponsorImageResIdKey, holder.getSponsorImageResId());
                    intent.putExtra(IndividualSponsorActivity.sponsorUrlKey, holder.getSponsor().getUrl());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() { return sponsors.size(); }

        public class SponsorHolder extends RecyclerView.ViewHolder {

            public Sponsor sponsor;
            public ImageView sponsorImage;
            public TextView sponsorName;
            public int sponsorImageResId;

            public SponsorHolder(View v) {
                super(v);
                sponsorImage = v.findViewById(R.id.sponsor_image);
                sponsorName = v.findViewById(R.id.sponsor_name);
            }

            public Sponsor getSponsor() {
                return sponsor;
            }

            public void setSponsor(Sponsor sponsor) {
                this.sponsor = sponsor;
            }

            public int getSponsorImageResId() {
                return sponsorImageResId;
            }

            public void setSponsorImageResId(int sponsorImageResId) {
                this.sponsorImageResId = sponsorImageResId;
            }
        }
    }
}
