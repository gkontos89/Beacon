package com.marshmallow.beacon.ui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.user.User;
import com.marshmallow.beacon.ui.BaseActivity;
import com.marshmallow.beacon.ui.marketing.SurveysActivity;

/**
 * Created by George on 7/13/2018.
 */
public class HomeActivity extends BaseActivity {

    // GUI handles
    ImageView profilePicture;
    TextView usernameText;
    TextView pointsTotalValue;
    TextView surveyCount;
    Button editProfileButton;
    CardView rewardsCatalogCard;
    CardView availableSurveysCard;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;
    private DatabaseReference userSurveyCountReference;
    private ValueEventListener userSurveyCountListener;

    // User
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        activityType = MainActivityTypes.HOME;
        super.onCreate(savedInstanceState);

        // GUI handle instantiation
        usernameText = findViewById(R.id.username_title);
        profilePicture = findViewById(R.id.home_profile_image);
        pointsTotalValue = findViewById(R.id.points_total_value);
        surveyCount = findViewById(R.id.survey_count);
        editProfileButton = findViewById(R.id.edit_profile_button);
        rewardsCatalogCard = findViewById(R.id.rewards_card);
        availableSurveysCard = findViewById(R.id.surveys_card);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();

        // User
        currentUser = UserManager.getInstance().getUser();

        // UI initialization
        if (currentUser.getProfilePicture() != null) {
            profilePicture.setImageBitmap(currentUser.getProfilePictureBitmap());
        }

        // TODO change to actual username one day...
        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        usernameText.setText(fullName);
        pointsTotalValue.setText(UserManager.getInstance().getUser().getPoints().toString());

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        rewardsCatalogCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO launch rewards catalog page
            }
        });

        availableSurveysCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SurveysActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeUserListeners() {
        // Grab user point total from the database.  no need to grab entire user here
        if (firebaseAuth.getUid() != null) {
            userSurveyCountReference = firebaseInst.getReference("distributedSurveys").child(firebaseAuth.getUid());
            userSurveyCountListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        surveyCount.setText(String.format("%d", dataSnapshot.getChildrenCount()));
                    } else {
                        surveyCount.setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            userSurveyCountReference.addValueEventListener(userSurveyCountListener);
        }
    }

    private void destroyUserListeners() {
        userSurveyCountReference.removeEventListener(userSurveyCountListener);
        userSurveyCountListener = null;
        userSurveyCountReference = null;
    }

    @Override
    public void onResume() {
        initializeUserListeners();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        destroyUserListeners();
    }

    @Override
    public void onDestroy() {
        UserManager.getInstance().signOutUser();
        super.onDestroy();
    }
}
