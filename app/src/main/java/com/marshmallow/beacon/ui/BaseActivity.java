package com.marshmallow.beacon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.ui.contacts.ContactsActivity;
import com.marshmallow.beacon.ui.marketing.SponsorsActivity;
import com.marshmallow.beacon.ui.user.HomeActivity;

/**
 * Created by George on 7/13/2018.
 */
public class BaseActivity extends AppCompatActivity {

    protected MainActivityTypes activityType;

    protected enum MainActivityTypes {
        HOME,
        CONTACTS,
        SPONSORS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home_button:
                        if (activityType != MainActivityTypes.HOME) {
                            finish();
                        }

                        break;

//                    case R.id.contacts_button:
//                        if (activityType != MainActivityTypes.CONTACTS) {
//                            if (activityType != MainActivityTypes.HOME) {
//                                finish();
//                            }
//
//                            Intent contactsIntent = new Intent(getApplicationContext(), ContactsActivity.class);
//                            startActivity(contactsIntent);
//                        }
//
//                        break;

                    case R.id.sponsors_button:
                        if (activityType != MainActivityTypes.SPONSORS) {
                            if (activityType != MainActivityTypes.HOME) {
                                finish();
                            }

                            Intent sponsorsIntent = new Intent(getApplicationContext(), SponsorsActivity.class);
                            startActivity(sponsorsIntent);
                        }

                        break;
                }

                return true;
            }
        });
    }
}
