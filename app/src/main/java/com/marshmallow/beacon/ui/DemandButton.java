package com.marshmallow.beacon.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.backend.BeaconBackend;

/**
 * Created by George on 7/22/2018.
 */
public class DemandButton extends BeaconButton {

    @StringRes int demandStatusOnText;
    @StringRes int demandStatusOffText;

    public DemandButton(Context context) {
        super(context);
    }

    @Override
    public void initialize(Boolean initialState, TextView statusTextView) {
        super.initialize(initialState, statusTextView);
        demandStatusOnText = R.string.supply_status_on_text;
        demandStatusOffText = R.string.supply_status_off_text;
        if (initialState) {
            this.statusTextView.setText(demandStatusOnText);
        } else {
            this.statusTextView.setText(demandStatusOffText);
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
                    statusTextView.setText(demandStatusOnText);
                    BeaconBackend.getInstance().setUserSupplyStatus(true);
                } else {
                    setText(getTextOff());
                    buttonView.setBackgroundResource(backgroundOff);
                    statusTextView.setText(demandStatusOffText);
                    BeaconBackend.getInstance().setUserSupplyStatus(false);
                }
            }
        });
    }
}
