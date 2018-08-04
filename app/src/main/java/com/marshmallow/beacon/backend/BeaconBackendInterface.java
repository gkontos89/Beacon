package com.marshmallow.beacon.backend;

import android.app.Activity;
import android.content.Context;

import com.marshmallow.beacon.models.Request;
import com.marshmallow.beacon.models.User;

/**
 * This interface establishes the contract for all backend communications for Beacon so that
 * different backends can be swapped out as needed
 *
 * Created by George on 7/15/2018.
 */
public interface BeaconBackendInterface {

    /**
     * Determines if a user is already signed into the application
     * @return true if user is currently signed in
     */
    Boolean isUserSignedIn();

    /**
     * Attempts to create a new user with email and password credentials
     */
    void createUserWithEmailAndPassword(Context context, Activity activity, String email, String password);

    /**
     * Attempts to sign in to an existing account using email and password credentials
     */
    void signInWithEmailAndPassword(Context context, Activity activity, String email, String password);

    /**
     * Signs out the current user from the back end services
     */
    void signOutUser();

    void submitProfileUpdates(User user);

    void sendNewContactRequest(final Context context, final Request request);

    void acceptRequest(Request request);

    void declineRequest(Request request);

    void confirmRequest(Request request);

    void cancelRequest(Request request);

    void clearRequest(Request request);
}
