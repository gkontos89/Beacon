package com.marshmallow.beacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CustomersActivityMain extends AppCompatActivity {

    // GUI Handles
    private CardView customerListCard;
    private CardView requestsCard;
    private TextView requestsCount;
    private ToggleButton supplyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_customers_main);
        super.onCreate(savedInstanceState);

        // GUI handle instantiation
        customerListCard = findViewById(R.id.customer_list_card);
        requestsCard = findViewById(R.id.requests_card);
        requestsCount = findViewById(R.id.request_count);
        supplyButton = findViewById(R.id.supply_button);

        requestsCount.setText("5");

        // TODO add functionality for customer and requests
        supplyButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                if (isChecked) {
                    button.setBackgroundResource(R.drawable.beacon_on);
                } else {
                    button.setBackgroundResource(R.drawable.beacon_off);
                }
            }
        });
    }
}
