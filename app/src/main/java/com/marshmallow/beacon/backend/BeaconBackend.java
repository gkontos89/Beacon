package com.marshmallow.beacon.backend;

import android.app.Activity;
import android.content.Context;

import com.marshmallow.beacon.models.contacts.Request;
import com.marshmallow.beacon.models.marketing.Sponsor;
import com.marshmallow.beacon.models.marketing.SurveyResult;
import com.marshmallow.beacon.models.user.User;

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
    public void submitProfileUpdates(User user) { backendHandle.submitProfileUpdates(user); }

    public void initializeContactListeners(Context context) { backendHandle.initializeContactListeners(context); }

    public void removeContactListeners() { backendHandle.removeContactListeners(); }

    @Override
    public void sendNewContactRequest(final Context context, final Request request) {
        backendHandle.sendNewContactRequest(context, request);
    }

    @Override
    public void acceptRequest(Request request) {
        backendHandle.acceptRequest(request);
    }

    @Override
    public void declineRequest(Request request) {
        backendHandle.declineRequest(request);
    }

    @Override
    public void confirmRequest(Request request) {
        backendHandle.confirmRequest(request);
    }

    @Override
    public void cancelRequest(Request request) {
        backendHandle.cancelRequest(request);
    }

    @Override
    public void clearRequest(Request request) {
        backendHandle.clearRequest(request);
    }

    @Override
    public void storeSponsorVisit(Sponsor sponsor) {
        backendHandle.storeSponsorVisit(sponsor);
    }

    @Override
    public void storeSurveyResult(SurveyResult surveyResult) {
        backendHandle.storeSurveyResult(surveyResult);
    }
}
