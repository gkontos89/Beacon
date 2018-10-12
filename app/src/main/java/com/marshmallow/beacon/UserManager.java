package com.marshmallow.beacon;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.models.contacts.Contact;
import com.marshmallow.beacon.models.user.User;

import java.util.HashMap;

/**
 * Created by George on 7/17/2018.
 */
public class UserManager {
    private static UserManager instance = null;
    private User user;
    private HashMap<String, Contact> contacts;
    private DatabaseReference userReference;
    private ValueEventListener userValueEventListener;

    private UserManager() {
    }

    public void initializeUserListener() {
        userReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());
        userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        userReference.addValueEventListener(userValueEventListener);
    }

    public void destroyUserListener() {
        userReference.removeEventListener(userValueEventListener);
        userValueEventListener = null;
        userReference = null;
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }

        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public HashMap<String, Contact> getContacts() {
        return contacts;
    }

    public void storeNewUser(User user) {
        userReference.setValue(user);
    }

    public void setUserSignInStatus(boolean signedIn) {
        userReference.child("signedIn").setValue(signedIn);
    }

    public void signOutUser() {
        userReference.child("signedIn").setValue(false);
        FirebaseAuth.getInstance().signOut();
        destroyUserListener();
    }

    public void addUserPoints(int points) {
        int newUserPoints = user.getPoints() + points;
        userReference.child("points").setValue(newUserPoints);
    }
}
