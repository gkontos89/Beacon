package com.marshmallow.beacon.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.backend.BeaconBackend;

/**
 * Created by George on 7/22/2018.
 */
public class SupplyButton extends BeaconButton {

    @StringRes int supplyStatusOnText;
    @StringRes int supplyStatusOffText;

    public SupplyButton(Context context) {
        super(context);
    }
    public SupplyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initialize(Boolean initialState, TextView statusTextView) {
        super.initialize(initialState, statusTextView);
        supplyStatusOnText = R.string.supply_status_on_text;
        supplyStatusOffText = R.string.supply_status_off_text;
        if (initialState) {
            this.statusTextView.setText(supplyStatusOnText);
        } else {
            this.statusTextView.setText(supplyStatusOffText);
        }

        initializeListeners();
    }

    protected void initializeListeners() {
        setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setText(getTextOn());
                    buttonView.setBackgroundResource(backgroundOn);
                    statusTextView.setText(supplyStatusOnText);
                    BeaconBackend.getInstance().setUserSupplyStatus(true);
                } else {
                    setText(getTextOff());
                    buttonView.setBackgroundResource(backgroundOff);
                    statusTextView.setText(supplyStatusOffText);
                    BeaconBackend.getInstance().setUserSupplyStatus(false);
                }
            }
        });
    }
}
