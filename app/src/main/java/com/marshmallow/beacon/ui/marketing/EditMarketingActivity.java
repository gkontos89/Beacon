package com.marshmallow.beacon.ui.marketing;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.MarketingManager;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.marketing.SponsorMarketValues;
import com.marshmallow.beacon.models.marketing.SurveyMarketValues;
import com.marshmallow.beacon.models.user.DataPoint;
import com.marshmallow.beacon.models.user.User;
import com.marshmallow.beacon.ui.ProfileUpdateManager;
import com.marshmallow.beacon.ui.user.HomeActivity;

/**
 * Created by George on 8/10/2018.
 */
public class EditMarketingActivity extends AppCompatActivity{

    // GUI handles
    private TextView pointsPerSurveyTextView;
    private TextView pointsPerSponsorTextView;
    private ToggleButton geolocationToggleButton;
    private ToggleButton firstNameToggleButton;
    private ToggleButton lastNameToggleButton;
    private ToggleButton birthdayToggleButton;
    private ToggleButton cityToggleButton;
    private ToggleButton stateToggleButton;
    private ToggleButton phoneToggleButton;
    private Button backButton;
    private Button submitButton;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;
    private DatabaseReference sponsorMarketValueReference;
    private ValueEventListener sponsorMarketValueEventListener;
    private DatabaseReference surveyMarketValueReference;
    private ValueEventListener surveyMarketValueEventListener;

    // Editable user
    private User editableUser;

    // Marketing models
    private SponsorMarketValues sponsorMarketValues;
    private SurveyMarketValues surveyMarketValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_marketing);

        // Firebase instantiation
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();

        // GUI instantiation
        pointsPerSponsorTextView = findViewById(R.id.points_per_sponsor_text_view);
        pointsPerSurveyTextView = findViewById(R.id.points_per_survey_text_view);
        geolocationToggleButton = findViewById(R.id.geolocation_toggle_button);
        firstNameToggleButton = findViewById(R.id.first_name_toggle_button);
        lastNameToggleButton = findViewById(R.id.last_name_toggle_button);
        birthdayToggleButton = findViewById(R.id.birthday_toggle_button);
        cityToggleButton = findViewById(R.id.city_toggle_button);
        stateToggleButton = findViewById(R.id.state_toggle_button);
        phoneToggleButton = findViewById(R.id.phone_toggle_button);

        backButton = findViewById(R.id.edit_marketing_back_button);
        submitButton = findViewById(R.id.edit_marketing_submit_button);

        // User instantiation
        editableUser = UserManager.getInstance().getUser();

        initializeEditProfileRelation(geolocationToggleButton, editableUser.getGeolocationOn());
        initializeEditProfileRelation(firstNameToggleButton, editableUser.getFirstName());
        initializeEditProfileRelation(lastNameToggleButton, editableUser.getLastName());
        initializeEditProfileRelation(birthdayToggleButton, editableUser.getBirthday());
        initializeEditProfileRelation(cityToggleButton, editableUser.getCity());
        initializeEditProfileRelation(stateToggleButton, editableUser.getState());
        initializeEditProfileRelation(phoneToggleButton, editableUser.getPhoneNumber());

        // Set up listeners for market values
        initializeMarketValueListeners();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProfileUpdates();
                ProfileUpdateManager.getInstance().destroyProfileUpdateActivities();
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        Intent activityStartIntent = getIntent();
        if (activityStartIntent.hasExtra("creatingAccount") && activityStartIntent.getBooleanExtra("creatingAccount", false)) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
    }

    private void submitProfileUpdates() {
        DatabaseReference userReference = firebaseInst.getReference("users");
        editableUser.setAccountCreationComplete(true);
        if (firebaseAuth.getUid() != null) {
            userReference.child(firebaseAuth.getUid()).setValue(editableUser);
        }
    }

    private void initializeMarketValueListeners() {
        sponsorMarketValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sponsorMarketValues = dataSnapshot.getValue(SponsorMarketValues.class);
                updateUserMarketingValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        surveyMarketValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                surveyMarketValues = dataSnapshot.getValue(SurveyMarketValues.class);
                updateUserMarketingValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        sponsorMarketValueReference = firebaseInst.getReference("sponsorMarketValues");
        sponsorMarketValueReference.addValueEventListener(sponsorMarketValueEventListener);
        surveyMarketValueReference = firebaseInst.getReference("surveyMarketValues");
        surveyMarketValueReference.addValueEventListener(surveyMarketValueEventListener);
    }

    private void initializeEditProfileRelation(ToggleButton toggleButton, final DataPoint dataPoint) {
        toggleButton.setChecked(dataPoint.getShared());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataPoint.setShared(isChecked);
                updateUserMarketingValue();
            }
        });
    }

    private void updateUserMarketingValue() {
        if (sponsorMarketValues != null) {
            pointsPerSponsorTextView.setText(MarketingManager.getInstance().getUserSponsorMarketingValue(editableUser, sponsorMarketValues).toString());
        }

        if (surveyMarketValues != null) {
            pointsPerSurveyTextView.setText(MarketingManager.getInstance().getUserSurveyMarketingValue(editableUser, surveyMarketValues).toString());
        }
    }
}
