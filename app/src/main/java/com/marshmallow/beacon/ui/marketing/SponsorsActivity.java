package com.marshmallow.beacon.ui.marketing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;
    private DatabaseReference sponsorsReference;
    private ChildEventListener sponsorChildEventListener;
    private DatabaseReference sponsorMarketValueReference;
    private ValueEventListener sponsorMarketValueEventListener;

    // Marketing models
    private SponsorMarketValues sponsorMarketValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sponsors);
        activityType = MainActivityTypes.SPONSORS;
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();
        initializeSponsorListeners();
        initializeMarketValueListeners();

        sponsors = new Vector<>();
        sponsorsRecyclerView = findViewById(R.id.sponsors_recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        sponsorsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        sponsorsAdapter = new SponsorsAdapter(sponsors);
        sponsorsRecyclerView.setAdapter(sponsorsAdapter);
    }

    private void initializeMarketValueListeners() {
        sponsorMarketValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sponsorMarketValues = dataSnapshot.getValue(SponsorMarketValues.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        sponsorMarketValueReference = firebaseInst.getReference("sponsorMarketValues");
        sponsorMarketValueReference.addValueEventListener(sponsorMarketValueEventListener);
    }

    private void initializeSponsorListeners() {
        // TODO use a firebase recycler view
        sponsorChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                sponsors.add(dataSnapshot.getValue(Sponsor.class));
                sponsorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                sponsors.add(dataSnapshot.getValue(Sponsor.class));
                sponsorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        sponsorsReference = firebaseInst.getReference("sponsors");
        sponsorsReference.addChildEventListener(sponsorChildEventListener);
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
            final Sponsor sponsor = sponsors.get(position);
            // TODO handle sponsor image from database.  For now it is hardcoded in App
            //holder.sponsorImage.setImageBitmap(sponsor.getProfilePictureBitmap());
            String sponsorName = sponsor.getName();
            holder.sponsorName.setText(sponsorName);
            switch (sponsorName) {
                case "At Home":
                    holder.sponsorImage.setImageResource(R.drawable.at_work_sports_bar_and_grill);
                    break;

                case "Brick and Motor Botique":
                    holder.sponsorImage.setImageResource(R.drawable.brick_and_motor_botique);
                    break;

                case "A Jacq of All Trades":
                    holder.sponsorImage.setImageResource(R.drawable.jacq_of_all_trades);
                    break;

                case "Make It Train":
                    holder.sponsorImage.setImageResource(R.drawable.make_it_train);
                    break;

                case "Rhodes Capital Management":
                    holder.sponsorImage.setImageResource(R.drawable.rhodes_capital_management);
                    break;
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storeSponsorVisit(sponsor);
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

    private void storeSponsorVisit(final Sponsor sponsor) {
        final User user = UserManager.getInstance().getUser();
        final DatabaseReference sponsorReference = firebaseInst.getReference().child("sponsors").child(sponsor.getUid());
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot(user);
        SponsorVisitEvent sponsorVisitEvent = new SponsorVisitEvent(userMarketDataSnapshot);
        String sponsorVisitKey = sponsorReference.child("sponsorVisitEvents").push().getKey();
        if (sponsorVisitKey != null) {
            sponsorReference.child("sponsorVisitEvents").child(sponsorVisitKey).setValue(sponsorVisitEvent);
        }

        // TODO show progress dialog with points calculated and added to profile
        // Check is user has hit this sponsor already
        sponsorReference.child("usersVisited").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Store that the user has visited the sponsor
                    if (firebaseAuth.getUid() != null) {
                        sponsorReference.child("usersVisited").child(firebaseAuth.getUid()).setValue(true);
                        // Update the user's points
                        Integer gainedUserPoints = user.getPoints() + MarketingManager.getInstance().getUserSponsorMarketingValue(user, sponsorMarketValues);
                        DatabaseReference userReference = firebaseInst.getReference().child("users").child(firebaseAuth.getUid());
                        userReference.child("points").setValue(gainedUserPoints);
                    }
                }

                visitSponsorSite(sponsor.getUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void visitSponsorSite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
