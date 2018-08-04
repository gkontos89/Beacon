package com.marshmallow.beacon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.marshmallow.beacon.R;

/**
 * Created by George on 8/2/2018.
 */
public class WelcomePrimaryActivity extends AppCompatActivity {

    // GUI handles
    private Button backButton;
    private Button nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // GUI instantiation
        backButton = findViewById(R.id.edit_profile_back_button);
        nextButton = findViewById(R.id.welcome_primary_next_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
