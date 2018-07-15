package com.marshmallow.beacon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by George on 7/13/2018.
 */
public class IndividualContactActivity extends AppCompatActivity {

    private TextView contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_contact);

        contactName = findViewById(R.id.contact_name);
        String contact = getIntent().getStringExtra("contactName");
        contactName.setText(contact);
    }
}
