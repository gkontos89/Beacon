package com.marshmallow.beacon.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.marshmallow.beacon.MarketingManager;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.backend.BeaconBackend;
import com.marshmallow.beacon.models.user.DataPoint;
import com.marshmallow.beacon.models.user.User;

import java.io.IOException;


/**
 * Created by George on 8/2/2018.
 */
public class EditProfileActivity extends AppCompatActivity {

    // GUI handles
    private TextView pointsPerSurveyTextView;
    private TextView pointsPerSponsorTextView;
    private ImageButton profileImageButton;
    private EditText firstNameEditText;
    private ToggleButton firstNameToggleButton;
    private EditText lastNameEditText;
    private ToggleButton lastNameToggleButton;
    private EditText emailEditText;
    private ToggleButton emailToggleButton;
    private EditText birthdayEditText;
    private ToggleButton birthdayToggleButton;
    private EditText cityEditText;
    private ToggleButton cityToggleButton;
    private EditText stateEditText;
    private ToggleButton stateToggleButton;
    private Button backButton;
    private Button submitButton;

    // Image handling
    private int PICK_IMAGE_REQUEST = 1;
    private boolean validImage = false;

    // Editable user
    private User editableUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // GUI instantiation
        pointsPerSponsorTextView = findViewById(R.id.points_per_sponsor_text_view);
        pointsPerSurveyTextView = findViewById(R.id.points_per_survey_text_view);
        profileImageButton = findViewById(R.id.edit_profile_image_button);
        firstNameEditText = findViewById(R.id.edit_profile_first_name_edit_text);
        firstNameToggleButton = findViewById(R.id.first_name_toggle_button);
        lastNameEditText = findViewById(R.id.edit_profile_last_name_edit_text);
        lastNameToggleButton = findViewById(R.id.last_name_toggle_button);
        emailEditText = findViewById(R.id.edit_profile_email_edit_text);
        emailToggleButton = findViewById(R.id.email_toggle_button);
        birthdayEditText = findViewById(R.id.edit_profile_birthday_edit_text);
        birthdayToggleButton = findViewById(R.id.birthday_toggle_button);
        cityEditText = findViewById(R.id.edit_profile_city_edit_text);
        cityToggleButton = findViewById(R.id.city_toggle_button);
        stateEditText = findViewById(R.id.edit_profile_state_edit_text);
        stateToggleButton = findViewById(R.id.state_toggle_button);
        backButton = findViewById(R.id.edit_profile_back_button);
        submitButton = findViewById(R.id.edit_profile_done_button);

        // User instantiation
        editableUser = UserManager.getInstance().getUser();

        updateUserMarketingValue();
        initializeEditProfileRelation(firstNameToggleButton, firstNameEditText, editableUser.getFirstName());
        initializeEditProfileRelation(lastNameToggleButton, lastNameEditText, editableUser.getLastName());
        initializeEditProfileRelation(emailToggleButton, emailEditText, editableUser.getEmail());
        initializeEditProfileRelation(birthdayToggleButton, birthdayEditText, editableUser.getBirthday());
        initializeEditProfileRelation(cityToggleButton, cityEditText, editableUser.getCity());
        initializeEditProfileRelation(stateToggleButton, stateEditText, editableUser.getState());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editableUser.getFirstName().setValue(firstNameEditText.getText().toString());
                editableUser.getLastName().setValue(lastNameEditText.getText().toString());
                editableUser.getEmail().setValue(emailEditText.getText().toString());
                editableUser.getBirthday().setValue(birthdayEditText.getText().toString());
                editableUser.getCity().setValue(cityEditText.getText().toString());
                editableUser.getState().setValue(stateEditText.getText().toString());
                BeaconBackend.getInstance().submitProfileUpdates(editableUser);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Image button initialization
        if (editableUser.getProfilePictureBitmap() != null) {
            profileImageButton.setImageBitmap(editableUser.getProfilePictureBitmap());
        }

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private void updateUserMarketingValue() {
        pointsPerSponsorTextView.setText(MarketingManager.getInstance().getUserSponsorMarketingValue(editableUser).toString());
        pointsPerSurveyTextView.setText(MarketingManager.getInstance().getUserSurveyMarketingValue(editableUser).toString());
    }

    private void initializeEditProfileRelation(ToggleButton toggleButton, final EditText editText, final DataPoint dataPoint) {
        toggleButton.setChecked(dataPoint.getShared());
        editText.setText(dataPoint.getValue());
        editText.setEnabled(dataPoint.getShared());

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataPoint.setShared(isChecked);
                editText.setEnabled(isChecked);
                updateUserMarketingValue();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileImageButton.setImageBitmap(bitmap);
                editableUser.setProfilePictureFromBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            validImage = true;
        }
    }
}