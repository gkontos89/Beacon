package com.marshmallow.beacon.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.backend.BeaconBackend;

/**
 * Created by George on 7/22/2018.
 */
public class BeaconButton extends ToggleButton {
    protected TextView statusTextView;
    protected @DrawableRes int backgroundOn;
    protected @DrawableRes int backgroundOff;

    public BeaconButton(Context context) {
        super(context);
    }

    public void initialize(Boolean initialState, TextView statusTextView) {
        this.statusTextView = statusTextView;
        backgroundOn = R.drawable.beacon_on;
        backgroundOff = R.drawable.beacon_off;
        setChecked(initialState);
        if (initialState) {
            setText(getTextOn());
            setBackgroundResource(backgroundOn);
        } else {
            setText(getTextOff());
            setBackgroundResource(backgroundOff);
        }
    }
}