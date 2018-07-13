package com.marshmallow.beacon;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by George on 7/13/2018.
 */
public class SuppliersActivityMain extends BaseActivity {

    // GUI handles
    private CardView supplierListCard;
    private CardView activeBeaconsCard;
    private TextView supplierBeaconCount;
    private ToggleButton demandButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_suppliers_main);
        super.onCreate(savedInstanceState);

        // GUI handle instantiation
        supplierListCard = findViewById(R.id.supplier_list_card);
        activeBeaconsCard = findViewById(R.id.active_beacons_card);
        supplierBeaconCount = findViewById(R.id.supplier_beacon_count);
        demandButton = findViewById(R.id.demand_button);

        supplierBeaconCount.setText("10");

        demandButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.beacon_on);
                } else {
                    buttonView.setBackgroundResource(R.drawable.beacon_off);
                }
            }
        });
    }
}
