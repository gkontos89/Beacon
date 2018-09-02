package com.marshmallow.beacon.ui.marketing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private HashMap<String, Sponsor> sponsorHashMap;

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

        sponsors = new Vector<>();
        sponsorHashMap = new HashMap<>();
        sponsorsRecyclerView = findViewById(R.id.sponsors_recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        sponsorsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        sponsorsAdapter = new SponsorsAdapter(sponsors);
        sponsorsRecyclerView.setAdapter(sponsorsAdapter);

        initializeSponsorListeners();
        initializeMarketValueListeners();
    }

    private void initializeSponsorListeners() {
        sponsorsReference = firebaseInst.getReference("sponsors");
        sponsorChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Sponsor sponsor = dataSnapshot.getValue(Sponsor.class);
                if (sponsor != null) {
                    sponsor.setVisited(true);
                    DatabaseReference userVisitReference = firebaseInst.getReference("sponsorVisitData").child(sponsor.getUid()).child("usersVisited").child(firebaseAuth.getUid());
                    userVisitReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                sponsor.setVisited(false);
                            }

                            sponsorHashMap.put(sponsor.getUid(), sponsor);
                            // TODO VERY SLOW AND INEFFICIENT...ok for now with only 5 advertisers
                            sponsors.clear();
                            sponsors.addAll(sponsorHashMap.values());
                            sponsorsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
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
                case "At Work Sports Bar":
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

            if (sponsor.getVisited()) {
                holder.sponsorVisitStatus.setVisibility(View.INVISIBLE);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSponsorClick(sponsor);
                }
            });
        }

        @Override
        public int getItemCount() { return sponsors.size(); }

        public class SponsorHolder extends RecyclerView.ViewHolder {

            public ImageView sponsorImage;
            public TextView sponsorName;
            public TextView sponsorVisitStatus;

            public SponsorHolder(View v) {
                super(v);
                sponsorImage = v.findViewById(R.id.sponsor_image);
                sponsorName = v.findViewById(R.id.sponsor_name);
                sponsorVisitStatus = v.findViewById(R.id.sponsor_visit_status);
            }
        }
    }

    private void handleSponsorClick(final Sponsor sponsor) {
        final User user = UserManager.getInstance().getUser();
        DatabaseReference sponsorVisitEventsReference = firebaseInst.getReference().child("sponsorVisitData").child(sponsor.getUid()).child("sponsorVisitEvents");
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot(user);
        SponsorVisitEvent sponsorVisitEvent = new SponsorVisitEvent(userMarketDataSnapshot);
        String sponsorVisitKey = sponsorVisitEventsReference.push().getKey();
        if (sponsorVisitKey != null) {
            sponsorVisitEventsReference.child(sponsorVisitKey).setValue(sponsorVisitEvent);
        }

        // Handle if the user hasn't visited this site yet
        if (!sponsor.getVisited()) {
            // Calculate earned points and store it in the user profile
            Integer earnedPoints = MarketingManager.getInstance().getUserSponsorMarketingValue(user, sponsorMarketValues);
            Integer newUserPoints = user.getPoints() + earnedPoints;
            DatabaseReference userReference = firebaseInst.getReference().child("users").child(firebaseAuth.getUid());
            user.setPoints(newUserPoints);
            userReference.child("points").setValue(newUserPoints);

            // Store that the user has visited the site
            DatabaseReference sponsorUserVisitReference = firebaseInst.getReference("sponsorVisitData").child(sponsor.getUid()).child("usersVisited");
            sponsorUserVisitReference.child(firebaseAuth.getUid()).setValue(true);

            // Change the sponsor status so that it will reflect on the card
            sponsor.setVisited(true);
            sponsorHashMap.put(sponsor.getUid(), sponsor);
            sponsors.clear();
            sponsors.addAll(sponsorHashMap.values());
            sponsorsAdapter.notifyDataSetChanged();

            // Show pop up window
            LayoutInflater inflater = (LayoutInflater) SponsorsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.sponsor_visit_popup, null);
            ((TextView)layout.findViewById(R.id.pop_point_total)).setText(String.format("%d Points", earnedPoints));
            final PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(findViewById(R.id.activity_sponsors_relative_layout), Gravity.CENTER, 0, 0);
            ((Button)layout.findViewById(R.id.pop_up_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    visitSponsorSite(sponsor.getUrl());
                }
            });
        } else {
            visitSponsorSite(sponsor.getUrl());
        }
    }

    private void visitSponsorSite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
