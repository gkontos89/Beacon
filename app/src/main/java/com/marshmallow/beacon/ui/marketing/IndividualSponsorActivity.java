package com.marshmallow.beacon.ui.marketing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
import com.marshmallow.beacon.models.user.User;

/**
 * Created by George on 9/12/2018.
 */
public class IndividualSponsorActivity extends AppCompatActivity {

    // GUI handles
    private ImageView sponsorImageView;
    private TextView sponsorNameTextView;
    private ImageView websiteImageView;
    private ImageView likeButton;
    private ImageView dislikeButton;
    private EditText postEditText;
    private Button postCommentButton;

    // Basic model data
    Sponsor sponsor;
    private SponsorMarketValues sponsorMarketValues;
    private int sponsorImageResId;
    private boolean sponsorVisited;
    private boolean likedTriggeredOnce;
    private boolean liked;

    // Intent data
    public static final String sponsorUidKey = "sponsorUid";
    public static final String sponsorNameKey = "sponsorName";
    public static final String sponsorImageResIdKey = "sponsorImageResId";
    public static final String sponsorUrlKey = "sponsorUrl";

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;
    private DatabaseReference userVisitDataReference;
    private DatabaseReference userWebVisitedDataReference;
    private DatabaseReference userLikeDataReference;
    private ValueEventListener userLikeStatusListener;
    private ValueEventListener userWebVisitedStatusListener;
    private DatabaseReference sponsorMarketValueReference;
    private ValueEventListener sponsorMarketValueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_sponsor);

        // Instantiate Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();

        // Instantiate GUI
        sponsorImageView = findViewById(R.id.sponsor_image);
        sponsorNameTextView = findViewById(R.id.sponsor_name);
        websiteImageView = findViewById(R.id.website_image);
        likeButton = findViewById(R.id.like_button);
        dislikeButton = findViewById(R.id.dislike_button);
        postEditText = findViewById(R.id.post_edit_text);
        postCommentButton = findViewById(R.id.post_button);

        // Retrieve intent data and populate GUI
        sponsor = new Sponsor();
        sponsor.setUid(getIntent().getStringExtra(sponsorUidKey));
        sponsor.setName(getIntent().getStringExtra(sponsorNameKey));
        sponsor.setUrl(getIntent().getStringExtra(sponsorUrlKey));
        sponsorImageResId = getIntent().getIntExtra(sponsorImageResIdKey, -1);

        sponsorNameTextView.setText(sponsor.getName());
        sponsorImageView.setImageResource(sponsorImageResId);

        // Retrieve database values for user visit and likes
        setupUserVisitDataListeners();
        initializeMarketValueListeners();

        // TODO set up timer to say loading sponsor data to prevent people from clicking web links before it is determined whether they've visited or not

        // Set up interaction listeners
        websiteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleWebsiteVisitInteraction();
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLikeInteractions(true);
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLikeInteractions(false);
            }
        });

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = postEditText.getText().toString();
                String newPostKey = userVisitDataReference.child("posts").push().getKey();

                // Store comment key in the user entry, and also post the full string to the sponsor entry
                if (newPostKey != null) {
                    userVisitDataReference.child("posts").child(newPostKey).setValue(true);
                    long timestamp = System.currentTimeMillis();
                    firebaseInst.getReference("sponsorVisitData").child(sponsor.getUid()).child("posts").child(newPostKey).child("timestamp").setValue(timestamp);
                    firebaseInst.getReference("sponsorVisitData").child(sponsor.getUid()).child("posts").child(newPostKey).child("text").setValue(comment);
                    Toast.makeText(IndividualSponsorActivity.this, "Commented posted", Toast.LENGTH_SHORT).show();
                    postEditText.setText("");
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyUserVisitDataListeners();
    }

    private void setupUserVisitDataListeners() {
        if (firebaseAuth.getUid() != null) {
            userVisitDataReference = firebaseInst.getReference("sponsorVisitData").child(sponsor.getUid()).child("usersVisited").child(firebaseAuth.getUid());
            userWebVisitedDataReference = userVisitDataReference.child("visited");
            userLikeDataReference = userVisitDataReference.child("like");

            userWebVisitedStatusListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        sponsorVisited = (boolean) dataSnapshot.getValue();
                        updateWebsiteVisitStatus(sponsorVisited);
                    } else {
                        sponsorVisited = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            userLikeStatusListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        likedTriggeredOnce = true;
                        liked = (boolean) dataSnapshot.getValue();
                        updateLikeStatuses(liked);
                    } else {
                        likedTriggeredOnce = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            userWebVisitedDataReference.addValueEventListener(userWebVisitedStatusListener);
            userLikeDataReference.addValueEventListener(userLikeStatusListener);
        }
    }

    private void destroyUserVisitDataListeners() {
        userWebVisitedDataReference.removeEventListener(userWebVisitedStatusListener);
        userLikeDataReference.removeEventListener(userLikeStatusListener);
        userWebVisitedStatusListener = null;
        userLikeStatusListener = null;
        userWebVisitedDataReference = null;
        userLikeDataReference = null;
        userVisitDataReference = null;
    }

    private void initializeMarketValueListeners() {
        // TODO move to sponsors having their own prices they are willing to pay out
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

    private void handleLikeInteractions(boolean liked) {
        userLikeDataReference.setValue(liked);
        if (!likedTriggeredOnce) {
            // Store user points
            User user = UserManager.getInstance().getUser();
            int newUserPoints = user.getPoints() + 1; // TODO get like/dislike market values
            DatabaseReference userReference = firebaseInst.getReference().child("users").child(firebaseAuth.getUid());
            userReference.child("points").setValue(newUserPoints);

            // Show pop up window
            LayoutInflater inflater = (LayoutInflater) IndividualSponsorActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.sponsor_like_pop_up, null);
            ((TextView)layout.findViewById(R.id.pop_point_total)).setText(String.format("%d Points", 1)); // TODO get like/dislike market values
            final PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(findViewById(R.id.activity_individual_sponsor_layout), Gravity.CENTER, 0, 0);

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
                }
            });
        }
    }

    private void handleWebsiteVisitInteraction() {
        userWebVisitedDataReference.setValue(true);
        if (!sponsorVisited) {
            // Store user points
            User user = UserManager.getInstance().getUser();
            int newUserPoints = user.getPoints() + MarketingManager.getInstance().getUserSponsorMarketingValue(user, sponsorMarketValues);
            DatabaseReference userReference = firebaseInst.getReference().child("users").child(firebaseAuth.getUid());
            userReference.child("points").setValue(newUserPoints);

            // Show pop up window
            LayoutInflater inflater = (LayoutInflater) IndividualSponsorActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.sponsor_visit_popup, null);
            ((TextView)layout.findViewById(R.id.pop_point_total)).setText(String.format("%d Points", 1)); // TODO get like/dislike market values
            final PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(findViewById(R.id.activity_individual_sponsor_layout), Gravity.CENTER, 0, 0);

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

    private void updateWebsiteVisitStatus(boolean visited) {
        if (visited) {
            websiteImageView.setBackgroundColor(Color.parseColor("#16a916"));
        }
    }

    private void updateLikeStatuses(boolean liked) {
        if (liked) {
            likeButton.setBackgroundColor(Color.parseColor("#5167ca"));
            dislikeButton.setBackgroundColor(Color.parseColor("#FFC1BFBF"));
        } else {
            likeButton.setBackgroundColor(Color.parseColor("#FFC1BFBF"));
            dislikeButton.setBackgroundColor(Color.parseColor("#9e090e"));
        }
    }
}
