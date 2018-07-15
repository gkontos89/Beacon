package com.marshmallow.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by George on 7/13/2018.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home_button:
                        Intent summaryIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(summaryIntent);
                        finish();
                        break;

                    case R.id.contacts_button:
                        Intent accountsIntent = new Intent(getApplicationContext(), ContactsActivity.class);
                        startActivity(accountsIntent);
                        finish();
                        break;
                }

                return true;
            }
        });
    }
}
