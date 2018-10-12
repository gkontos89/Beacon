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
import com.google.firebase.database.FirebaseDatabase;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.user.User;

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
    private EditText phoneEditText;
    private Button backButton;
    private Button submitButton;

    // Firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseInst;

    // Image handling
    private int PICK_IMAGE_REQUEST = 1;
    // TODO change to false once images are better
    private boolean validImage = true;

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
        phoneEditText = findViewById(R.id.edit_profile_phone_number_edit_text);
        backButton = findViewById(R.id.edit_profile_back_button);
        submitButton = findViewById(R.id.edit_profile_submit_button);

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileIsValid()) {
                    User updatedUser = new User();
                    updatedUser.setFirstName(firstNameEditText.getText().toString());
                    updatedUser.setLastName(lastNameEditText.getText().toString());
                    updatedUser.setBirthday(birthdayEditText.getText().toString());
                    updatedUser.setCity(cityEditText.getText().toString());
                    updatedUser.setState(stateEditText.getText().toString());
                    updatedUser.setPhoneNumber(phoneEditText.getText().toString());
                    updatedUser.setAccountCreationComplete(true);
                    updatedUser.setPoints(editableUser.getPoints());
                    UserManager.getInstance().storeNewUser(updatedUser);

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill out all profile entries", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeTextFields() {
        firstNameEditText.setText(editableUser.getFirstName());
        lastNameEditText.setText(editableUser.getLastName());
        birthdayEditText.setText(editableUser.getBirthday());
        cityEditText.setText(editableUser.getCity());
        stateEditText.setText(editableUser.getState());
        phoneEditText.setText(editableUser.getPhoneNumber());
    }

    private boolean profileIsValid() {
        boolean profileIsValid = true;
        // TODO bring back profile pictures
//        if (!validImage) {
//            profileIsValid = false;
//            Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show();
//        }

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

        if (phoneEditText.getText().toString().isEmpty()) {
            profileIsValid = false;
            phoneEditText.setError("Phone number cannot be empty");
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
