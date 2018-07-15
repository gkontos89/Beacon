package com.marshmallow.beacon.ui;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.marshmallow.beacon.R;

/**
 * Created by George on 7/13/2018.
 */
public class HomeActivity extends BaseActivity {

    // GUI handles
    private ToggleButton supplyButton;
    private TextView supplyStatusText;
    private ToggleButton demandButton;
    private TextView demandStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        // GUI handle instantiation
        supplyButton = findViewById(R.id.supply_button);
        supplyStatusText = findViewById(R.id.supply_status_text);
        demandButton = findViewById(R.id.demand_button);
        demandStatusText = findViewById(R.id.demand_status_text);

        demandButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.beacon_on);
                    demandStatusText.setText(R.string.demand_status_on_text);
                } else {
                    buttonView.setBackgroundResource(R.drawable.beacon_off);
                    demandStatusText.setText(R.string.demand_status_off_text);
                }
            }
        });

        supplyButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.beacon_on);
                    supplyStatusText.setText(R.string.supply_status_on_text);
                } else {
                    buttonView.setBackgroundResource(R.drawable.beacon_off);
                    supplyStatusText.setText(R.string.supply_status_off_text);
                }
            }
        });
    }
}
