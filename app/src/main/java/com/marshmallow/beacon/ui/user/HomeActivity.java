package com.marshmallow.beacon.ui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.user.User;
import com.marshmallow.beacon.ui.BaseActivity;

/**
 * Created by George on 7/13/2018.
 */
public class HomeActivity extends BaseActivity {

    // GUI handles
    ImageView profilePicture;
    TextView usernameText;
    TextView pointsTotalValue;
    Button editProfileButton;
    CardView rewardsCatalogCard;
    CardView availableSurveysCard;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;

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
                // TODO launch surveys page
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (firebaseAuth.getUid() != null) {
            DatabaseReference userReference = firebaseInst.getReference("users").child(firebaseAuth.getUid());
            userReference.child("signedIn").setValue(false);
            firebaseAuth.signOut();
        }
    }
}
