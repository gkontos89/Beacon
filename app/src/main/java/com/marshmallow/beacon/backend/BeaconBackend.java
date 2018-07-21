package com.marshmallow.beacon.backend;

import android.app.Activity;
import android.content.Context;

import com.marshmallow.beacon.models.User;

/**
 * BeehiveBackend is the front facing API's that are used throughout the application.
 * It implements the BeehiveBackendInterface so that all different backend models can be dropped in
 * and instantiated as the new backend model
 *
 * Created by George on 4/29/2018.
 */
public class BeaconBackend implements BeaconBackendInterface {
    private static BeaconBackend instance = null;
    private FirebaseBackend backendHandle;

    private BeaconBackend() {
        backendHandle = new FirebaseBackend();
    }

    public static BeaconBackend getInstance() {
        if (instance == null) {
            instance = new BeaconBackend();
        }

        return instance;
    }

    @Override
    public Boolean isUserSignedIn() {
        return backendHandle.isUserSignedIn();
    }

    @Override
    public void createUserWithEmailAndPassword(Context context, Activity activity, String email, String password) {
        backendHandle.createUserWithEmailAndPassword(context, activity, email, password);
    }

    @Override
    public void signInWithEmailAndPassword(Context context, Activity activity, String email, String password) {
        backendHandle.signInWithEmailAndPassword(context, activity, email, password);
    }

    @Override
    public void signOutUser() {
        backendHandle.signOutUser();
    }

    @Override
    public void loadUserData(Context context, Activity activity) {
        backendHandle.loadUserData(context, activity);
    }

    @Override
    public void setUserDemandStatus(Boolean status) {
        backendHandle.setUserDemandStatus(status);
    }

    @Override
    public void setUserSupplyStatus(Boolean status) {
        backendHandle.setUserSupplyStatus(status);
    }
}
