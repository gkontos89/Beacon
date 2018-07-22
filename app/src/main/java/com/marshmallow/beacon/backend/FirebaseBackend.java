package com.marshmallow.beacon.backend;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.broadcasts.CreateUserStatusBroadcast;
import com.marshmallow.beacon.broadcasts.SignInStatusBroadcast;
import com.marshmallow.beacon.models.CommunityEvent;
import com.marshmallow.beacon.models.User;
import com.marshmallow.beacon.models.UserEvent;

/**
 * This class implements Firebase backend communications
 *
 * Created by George on 4/29/2018.
 */
public class FirebaseBackend implements BeaconBackendInterface{
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userReference = null;
    private DatabaseReference statsSupplyTotalRef = null;
    private DatabaseReference statsDemandTotalRef = null;
    private Integer supplyTotal;
    private Integer demandTotal;

    public FirebaseBackend() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initializeUserListeners() {
        userReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserManager.getInstance().setUser(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeStatsListeners() {
        statsSupplyTotalRef = FirebaseDatabase.getInstance().getReference("stats/supplyTotal");
        statsDemandTotalRef = FirebaseDatabase.getInstance().getReference("stats/demandTotal");

        statsSupplyTotalRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                supplyTotal = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        statsDemandTotalRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                demandTotal = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
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
                            initializeUserListeners();
                            initializeStatsListeners();
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
        userReference = null;
        statsDemandTotalRef = null;
        statsSupplyTotalRef = null;
    }

    private void storeNewUser(String username) {
        User user = new User(username);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseAuth.getUid()).setValue(user);
        initializeUserListeners();
        initializeStatsListeners();
    }

    @Override
    public void setUserSupplyStatus(Boolean status) {
        // TODO handle timestamp
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseAuth.getUid()).child("supplyStatus").setValue(status);
        storeUserEvent();

        CommunityEvent communityEvent = new CommunityEvent();
        // Lock down the demandTotal and supplyTotal seen at this event time
        Integer demandTotal = this.demandTotal;
        Integer supplyTotal = this.supplyTotal;
        communityEvent.setDemandTotal(demandTotal);
        if (status) {
            supplyTotal +=1;
        } else {
            supplyTotal -=1;
        }

        communityEvent.setSupplyTotal(supplyTotal);
        communityEvent.setTimestamp(55);
        storeCommunityEvent(communityEvent);
    }

    @Override
    public void setUserDemandStatus(Boolean status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseAuth.getUid()).child("demandStatus").setValue(status);
        storeUserEvent();

        CommunityEvent communityEvent = new CommunityEvent();
        // Lock down the demandTotal and supplyTotal seen at this event time
        Integer demandTotal = this.demandTotal;
        Integer supplyTotal = this.supplyTotal;
        communityEvent.setSupplyTotal(supplyTotal);
        if (status) {
            demandTotal +=1;
        } else {
            demandTotal -=1;
        }

        communityEvent.setDemandTotal(demandTotal);
        communityEvent.setTimestamp(22);
        storeCommunityEvent(communityEvent);
    }

    @Override
    public void storeCommunityEvent(CommunityEvent communityEvent) {
        // Store the unique event
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("stats/communityEvents");
        databaseReference.push().setValue(communityEvent);
    }

    @Override
    public void storeUserEvent() {
        // TODO handle timestamp
        // Create the event
        UserEvent userEvent = new UserEvent();
        userEvent.setTimestamp(33);
        userEvent.setUserUniqueId(firebaseAuth.getUid());
        userEvent.setDemandStatus(UserManager.getInstance().getUser().getDemandStatus());
        userEvent.setSupplyStatus(UserManager.getInstance().getUser().getSupplyStatus());

        // Store the unique event
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("stats/userEvents");
        databaseReference.push().setValue(userEvent);
    }
}
