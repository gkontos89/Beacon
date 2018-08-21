package com.marshmallow.beacon.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.user.User;
import com.marshmallow.beacon.ui.marketing.EditMarketingActivity;

import java.io.IOException;


/**
 * Created by George on 8/2/2018.
 */
public class EditProfileActivity extends AppCompatActivity {

    // GUI handles
    private ImageButton profileImageButton;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText birthdayEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private Button backButton;
    private Button nextButton;

    // Firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseInst;

    // Image handling
    private int PICK_IMAGE_REQUEST = 1;
    private boolean validImage = false;

    // Editable user
    private User editableUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Firebase instantiation
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();

        // GUI instantiation
        profileImageButton = findViewById(R.id.edit_profile_image_button);
        firstNameEditText = findViewById(R.id.edit_profile_first_name_edit_text);
        lastNameEditText = findViewById(R.id.edit_profile_last_name_edit_text);
        birthdayEditText = findViewById(R.id.edit_profile_birthday_edit_text);
        cityEditText = findViewById(R.id.edit_profile_city_edit_text);
        stateEditText = findViewById(R.id.edit_profile_state_edit_text);
        backButton = findViewById(R.id.edit_profile_back_button);
        nextButton = findViewById(R.id.edit_profile_next_button);

        // User instantiation
        editableUser = UserManager.getInstance().getUser();

        // Image button initialization
        if (editableUser.getProfilePictureBitmap() != null) {
            validImage = true;
            profileImageButton.setImageBitmap(editableUser.getProfilePictureBitmap());
        }

        // Edit text setup
        initializeTextFields();

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileIsValid()) {
                    editableUser.getFirstName().setValue(firstNameEditText.getText().toString());
                    editableUser.getLastName().setValue(lastNameEditText.getText().toString());
                    editableUser.getBirthday().setValue(birthdayEditText.getText().toString());
                    editableUser.getCity().setValue(cityEditText.getText().toString());
                    editableUser.getState().setValue(stateEditText.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), EditMarketingActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill out all profile entries", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeTextFields() {
        firstNameEditText.setText(editableUser.getFirstName().getValue());
        lastNameEditText.setText(editableUser.getLastName().getValue());
        birthdayEditText.setText(editableUser.getBirthday().getValue());
        cityEditText.setText(editableUser.getCity().getValue());
        stateEditText.setText(editableUser.getState().getValue());
    }

    private boolean profileIsValid() {
        boolean profileIsValid = true;
        if (!validImage) {
            profileIsValid = false;
        }

        if (firstNameEditText.getText().toString().isEmpty()) {
            profileIsValid = false;
            firstNameEditText.setError("First name cannot be empty!");
        }

        if (lastNameEditText.getText().toString().isEmpty()) {
            profileIsValid = false;
            lastNameEditText.setError("Last name cannot be empty!");
        }

        if (birthdayEditText.getText().toString().isEmpty()) {
            profileIsValid = false;
            birthdayEditText.setError("Birthday name cannot be empty!");
        }

        if (cityEditText.getText().toString().isEmpty()) {
            profileIsValid = false;
            cityEditText.setError("City name cannot be empty!");
        }

        if (stateEditText.getText().toString().isEmpty()) {
            profileIsValid = false;
            stateEditText.setError("State name cannot be empty!");
        }

        return profileIsValid;
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
