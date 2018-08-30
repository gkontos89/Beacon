package com.marshmallow.beacon.ui;

import android.app.Activity;

import com.marshmallow.beacon.ui.user.EditProfileActivity;

import java.util.Vector;

/**
 * Created by George on 8/24/2018.
 */
public class ProfileUpdateManager {
    private static ProfileUpdateManager instance = null;
    private Vector<Activity> profileUpdateActivities;

    private ProfileUpdateManager() {
        profileUpdateActivities = new Vector<>();
    }

    public static ProfileUpdateManager getInstance() {
        if (instance == null) {
            instance = new ProfileUpdateManager();
        }

        return instance;
    }

    public void addProfileUpdateActivity(Activity activity) {
        profileUpdateActivities.add(activity);
    }

    public void destroyProfileUpdateActivities() {
        for (Activity activity : profileUpdateActivities) {
            activity.finish();
        }

        profileUpdateActivities.clear();
    }


    public void removeProfileUpdateActivity(Activity self) {
        profileUpdateActivities.remove(self);
    }
}
