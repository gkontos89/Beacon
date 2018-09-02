package com.marshmallow.beacon.ui.marketing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
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
import android.view.WindowManager;
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
import java.util.Map;
import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SponsorsActivity extends BaseActivity {

    // GUI handles
    private RecyclerView sponsorsRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SponsorsAdapter sponsorsAdapter;
    private Vector<SponsorHandler> sponsors;
    private HashMap<String, SponsorHandler> sponsorHashMap;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;
    private DatabaseReference sponsorsReference;
    private ChildEventListener sponsorChildEventListener;
    private DatabaseReference sponsorMarketValueReference;
    private ValueEventListener sponsorMarketValueEventListener;

    // BroadcastReceiver
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private static final String sponsorsRetrievedAction = "sponsorsRetrieved";

    // Marketing models
    private SponsorMarketValues sponsorMarketValues;
    private int sponsorCount;

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

//        initializeSponsorBroadcastReceiver();
        initializeSponsorListeners();
        initializeMarketValueListeners();
    }

    private class SponsorHandler {
        private Sponsor sponsor;
        private DatabaseReference userVisitReference;
        private ValueEventListener userVisitListener;

        SponsorHandler(Sponsor sponsor) {
            this.sponsor = sponsor;
            this.sponsor.setVisited(true);
            setupUserVisitListeners();
        }

        private void setupUserVisitListeners() {
            userVisitReference = firebaseInst.getReference("sponsorVisitData").child(sponsor.getUid()).child("usersVisited").child(firebaseAuth.getUid());
            userVisitListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        sponsor.setVisited(false);
                    }

                    sponsors.clear();
                    sponsors.addAll(sponsorHashMap.values());
                    sponsorCount--;

                    if (sponsorCount == 0) {
                        sponsorsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            userVisitReference.addListenerForSingleValueEvent(userVisitListener);
        }

        public Sponsor getSponsor() {
            return sponsor;
        }

        public String getSponsorUid() {
            return sponsor.getUid();
        }
    }

//    private void initializeSponsorBroadcastReceiver() {
//        intentFilter = new IntentFilter();
//        intentFilter.addAction(sponsorsRetrievedAction);
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                for (Map.Entry<String, Sponsor> entry : sponsorHashMap.entrySet()) {
//                    final String sponsorUid = entry.getKey();
//                    DatabaseReference userVisitReference = firebaseInst.getReference("sponsorVisitData").child(sponsorUid).child("usersVisited").child(firebaseAuth.getUid());
//                    userVisitReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (!dataSnapshot.exists()) {
//                                Sponsor sponsor = sponsorHashMap.get(sponsorUid);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//        };
//
//        registerReceiver(broadcastReceiver, intentFilter);
//    }

    private void initializeSponsorListeners() {
        sponsorCount = -1;
        sponsorsReference = firebaseInst.getReference("sponsors");
        sponsorsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sponsorCount = (int) dataSnapshot.getChildrenCount();
                sponsorChildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        SponsorHandler sponsorHandler = new SponsorHandler(dataSnapshot.getValue(Sponsor.class));
                        sponsorHashMap.put(sponsorHandler.getSponsorUid(), sponsorHandler);
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

        private Vector<SponsorHandler> sponsors;

        public SponsorsAdapter(Vector<SponsorHandler> sponsors) {
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
            final SponsorHandler sponsorHandler = sponsors.get(position);
            // TODO handle sponsor image from database.  For now it is hardcoded in App
            //holder.sponsorImage.setImageBitmap(sponsor.getProfilePictureBitmap());
            String sponsorName = sponsorHandler.getSponsor().getName();
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

            if (sponsorHandler.getSponsor().getVisited()) {
                holder.sponsorVisitStatus.setVisibility(View.INVISIBLE);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSponsorClick(sponsorHandler);
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

    private void handleSponsorClick(final SponsorHandler sponsorHandler) {
        final User user = UserManager.getInstance().getUser();
        DatabaseReference sponsorVisitEventsReference = firebaseInst.getReference().child("sponsorVisitData").child(sponsorHandler.getSponsor().getUid()).child("sponsorVisitEvents");
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot(user);
        SponsorVisitEvent sponsorVisitEvent = new SponsorVisitEvent(userMarketDataSnapshot);
        String sponsorVisitKey = sponsorVisitEventsReference.push().getKey();
        if (sponsorVisitKey != null) {
            sponsorVisitEventsReference.child(sponsorVisitKey).setValue(sponsorVisitEvent);
        }

        // Handle if the user hasn't visited this site yet
        if (!sponsorHandler.getSponsor().getVisited()) {
            // Calculate earned points and store it in the user profile
            Integer earnedPoints = MarketingManager.getInstance().getUserSponsorMarketingValue(user, sponsorMarketValues);
            Integer newUserPoints = user.getPoints() + earnedPoints;
            DatabaseReference userReference = firebaseInst.getReference().child("users").child(firebaseAuth.getUid());
            user.setPoints(newUserPoints);
            userReference.child("points").setValue(newUserPoints);

            // Store that the user has visited the site
            DatabaseReference sponsorUserVisitReference = firebaseInst.getReference("sponsorVisitData").child(sponsorHandler.getSponsor().getUid()).child("usersVisited");
            sponsorUserVisitReference.child(firebaseAuth.getUid()).setValue(true);

            // Change the sponsor status so that it will reflect on the card
            sponsorHandler.getSponsor().setVisited(true);
            sponsorHashMap.put(sponsorHandler.getSponsor().getUid(), sponsorHandler);
            sponsors.clear();
            sponsors.addAll(sponsorHashMap.values());
            sponsorsAdapter.notifyDataSetChanged();

            // Show pop up window
            LayoutInflater inflater = (LayoutInflater) SponsorsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.sponsor_visit_popup, null);
            ((TextView)layout.findViewById(R.id.pop_point_total)).setText(String.format("%d Points", earnedPoints));
            final PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(findViewById(R.id.activity_sponsors_relative_layout), Gravity.CENTER, 0, 0);

            // Dim the background
            final View container;
            if (popupWindow.getBackground() == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    container = (View) popupWindow.getContentView().getParent();
                } else {
                    container = popupWindow.getContentView();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    container = (View) popupWindow.getContentView().getParent().getParent();
                } else {
                    container = (View) popupWindow.getContentView().getParent();
                }
            }

            Context context = popupWindow.getContentView().getContext();
            final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            final WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.75f;
            windowManager.updateViewLayout(container, layoutParams);


            (layout.findViewById(R.id.pop_up_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Un-dim the background
                    layoutParams.dimAmount = 0.0f;
                    windowManager.updateViewLayout(container, layoutParams);
                    popupWindow.dismiss();
                    visitSponsorSite(sponsorHandler.getSponsor().getUrl());
                }
            });
        } else {
            visitSponsorSite(sponsorHandler.getSponsor().getUrl());
        }
    }

    private void visitSponsorSite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
