package com.marshmallow.beacon.ui;

import android.content.Intent;
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

    // GUI handles
    private TextView usernameText;
    private ToggleButton supplyButton;
    private TextView supplyStatusText;
    private ToggleButton demandButton;
    private TextView demandStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        // GUI handle instantiation
        usernameText = findViewById(R.id.username_title);
        supplyButton = findViewById(R.id.supply_button);
        supplyStatusText = findViewById(R.id.supply_status_text);
        demandButton = findViewById(R.id.demand_button);
        demandStatusText = findViewById(R.id.demand_status_text);

        // TODO initialize buttons from database values
        usernameText.setText(UserManager.getInstance().getUser().getUsername());

        Boolean inDemand = UserManager.getInstance().getUser().getDemandStatus();
        demandButton.setChecked(inDemand);
        // TODO wrap these status buttons in a class
        if (inDemand) {
            demandButton.setBackgroundResource(R.drawable.beacon_on);
            demandStatusText.setText(R.string.demand_status_on_text);
        } else {
            demandButton.setBackgroundResource(R.drawable.beacon_off);
            demandStatusText.setText(R.string.demand_status_off_text);
        }

        Boolean inSupply = UserManager.getInstance().getUser().getSupplyStatus();
        supplyButton.setChecked(inSupply);
        if (inSupply) {
            supplyButton.setBackgroundResource(R.drawable.beacon_on);
            supplyStatusText.setText(R.string.supply_status_on_text);
        } else {
            supplyButton.setBackgroundResource(R.drawable.beacon_off);
            supplyStatusText.setText(R.string.supply_status_off_text);
        }

        demandButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.beacon_on);
                    demandStatusText.setText(R.string.demand_status_on_text);
                    BeaconBackend.getInstance().setUserDemandStatus(true);
                } else {
                    buttonView.setBackgroundResource(R.drawable.beacon_off);
                    demandStatusText.setText(R.string.demand_status_off_text);
                    BeaconBackend.getInstance().setUserDemandStatus(false);
                }
            }
        });

        supplyButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.beacon_on);
                    supplyStatusText.setText(R.string.supply_status_on_text);
                    BeaconBackend.getInstance().setUserSupplyStatus(true);
                } else {
                    buttonView.setBackgroundResource(R.drawable.beacon_off);
                    supplyStatusText.setText(R.string.supply_status_off_text);
                    BeaconBackend.getInstance().setUserSupplyStatus(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BeaconBackend.getInstance().signOutUser();
    }
}
