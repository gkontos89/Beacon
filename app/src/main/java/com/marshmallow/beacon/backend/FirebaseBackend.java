package com.marshmallow.beacon.backend;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.broadcasts.CreateUserStatusBroadcast;
import com.marshmallow.beacon.broadcasts.LoadUserStatusBroadcast;
import com.marshmallow.beacon.broadcasts.SignInStatusBroadcast;
import com.marshmallow.beacon.models.User;

/**
 * This class implements Firebase backend communications
 *
 * Created by George on 4/29/2018.
 */
public class FirebaseBackend implements BeaconBackendInterface{
    private FirebaseAuth firebaseAuth;

    public FirebaseBackend() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public Boolean isUserSignedIn() {
        return (firebaseAuth.getCurrentUser() != null);
    }

    @Override
    public void createUserWithEmailAndPassword(final Context context, final Activity activity, final String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // TODO change to username
                            storeNewUser(email);
                            CreateUserStatusBroadcast createUserStatusBroadcast = new CreateUserStatusBroadcast(null, null);
                            Intent intent = createUserStatusBroadcast.getSuccessfulBroadcast();
                            context.sendBroadcast(intent);
                        } else {
                            CreateUserStatusBroadcast createUserStatusBroadcast = new CreateUserStatusBroadcast(task.getException().getMessage(), null);
                            Intent intent = createUserStatusBroadcast.getFailureBroadcast();
                            context.sendBroadcast(intent);
                        }


                    }
                });
    }

    @Override
    public void signInWithEmailAndPassword(final Context context, final Activity activity, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SignInStatusBroadcast signInStatusBroadcast = new SignInStatusBroadcast(null, null);
                            Intent intent = signInStatusBroadcast.getSuccessfulBroadcast();
                            context.sendBroadcast(intent);
                        } else {
                            SignInStatusBroadcast signInStatusBroadcast = new SignInStatusBroadcast(task.getException().getMessage(), null);
                            Intent intent = signInStatusBroadcast.getFailureBroadcast();
                            context.sendBroadcast(intent);
                        }
                    }
                });
    }

    @Override
    public void signOutUser() {
        firebaseAuth.signOut();
    }

    @Override
    public void loadUserData(final Context context, final Activity activity) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserManager.getInstance().setUser(dataSnapshot.getValue(User.class));
                LoadUserStatusBroadcast loadUserStatusBroadcast = new LoadUserStatusBroadcast(null, null);
                Intent intent = loadUserStatusBroadcast.getSuccessfulBroadcast();
                context.sendBroadcast(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                LoadUserStatusBroadcast loadUserStatusBroadcast = new LoadUserStatusBroadcast(databaseError.getMessage(), null);
                Intent intent = loadUserStatusBroadcast.getFailureBroadcast();
                context.sendBroadcast(intent);
            }
        });
    }

    private void storeNewUser(String username) {
        User user = new User(username);
        UserManager.getInstance().setUser(user);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseAuth.getUid()).setValue(user);
    }

    private void attachUserListeners() {

    }

    @Override
    public void setUserSupplyStatus(Boolean status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseAuth.getUid()).child("supplyStatus").setValue(status);
    }

    @Override
    public void setUserDemandStatus(Boolean status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseAuth.getUid()).child("demandStatus").setValue(status);
    }
}
