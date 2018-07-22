package com.marshmallow.beacon.ui;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.backend.BeaconBackend;

/**
 * Created by George on 7/13/2018.
 */
public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        // GUI handle instantiation
        TextView usernameText = findViewById(R.id.username_title);
        BeaconButton supplyButton = findViewById(R.id.supply_button);
        TextView supplyStatusText = findViewById(R.id.supply_status_text);
        BeaconButton demandButton = findViewById(R.id.demand_button);
        TextView demandStatusText = findViewById(R.id.demand_status_text);

        // UI initialization
        supplyButton.initialize(UserManager.getInstance().getUser().getSupplyStatus(), supplyStatusText);
        demandButton.initialize(UserManager.getInstance().getUser().getDemandStatus(), demandStatusText);
        usernameText.setText(UserManager.getInstance().getUser().getUsername());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BeaconBackend.getInstance().signOutUser();
    }
}
