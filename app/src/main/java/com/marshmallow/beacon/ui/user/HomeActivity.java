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

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.backend.BeaconBackend;
import com.marshmallow.beacon.broadcasts.LoadUserStatusBroadcast;
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
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

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

        // UI initialization
        // TODO check if these values are valid?
        if (UserManager.getInstance().getUser().getProfilePicture() != null) {
            profilePicture.setImageBitmap(UserManager.getInstance().getUser().getProfilePictureBitmap());
        }

        usernameText.setText(UserManager.getInstance().getUser().getUsername());
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

        // Setup broadcast receivers for surveys and point totals
        // TODO setup intent filter to catch survey broadcasts
        intentFilter = new IntentFilter();
        intentFilter.addAction(LoadUserStatusBroadcast.action);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null) {
                    if (intent.getAction().equals(LoadUserStatusBroadcast.action)) {
                        if (intent.getStringExtra(LoadUserStatusBroadcast.statusKey).equals(LoadUserStatusBroadcast.USER_LOADED_SUCCESSFUL)) {
                            pointsTotalValue.setText(UserManager.getInstance().getUser().getPoints().toString());
                        }
                    }
                }
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BeaconBackend.getInstance().signOutUser();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }
}
