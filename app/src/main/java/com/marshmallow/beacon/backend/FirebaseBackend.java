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
import com.marshmallow.beacon.broadcasts.LoadUserStatusBroadcast;
import com.marshmallow.beacon.broadcasts.SignInStatusBroadcast;
import com.marshmallow.beacon.broadcasts.StatusBroadcast;
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

    // TODO bring back loading status

    private void initializeUserListeners(final Context context) {
        userReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid());
        userReference.addValueEventListener(new ValueEventListener() {
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

    private void initializeStatsListeners() {
        statsSupplyTotalRef = FirebaseDatabase.getInstance().getReference("stats/supplyTotal");
        statsDemandTotalRef = FirebaseDatabase.getInstance().getReference("stats/demandTotal");

        statsSupplyTotalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supplyTotal = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        statsDemandTotalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                demandTotal = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
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
                            storeNewUser(email);
                            initializeUserListeners(context);
                            initializeStatsListeners();
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
                            initializeUserListeners(context);
                            initializeStatsListeners();
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
    }

    @Override
    public void setUserSupplyStatus(Boolean status) {
        // TODO handle timestamp
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseAuth.getUid()).child("supplyStatus").setValue(status);
        UserEvent userEvent = new UserEvent();
        userEvent.setTimestamp(33);
        userEvent.setUserUniqueId(firebaseAuth.getUid());
        userEvent.setSupplyStatus(status);
        userEvent.setDemandStatus(UserManager.getInstance().getUser().getDemandStatus());
        storeUserEvent(userEvent);

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

        // Handle user events for changes in Demand
        UserEvent userEvent = new UserEvent();
        userEvent.setTimestamp(33);
        userEvent.setUserUniqueId(firebaseAuth.getUid());
        userEvent.setDemandStatus(status);
        userEvent.setSupplyStatus(UserManager.getInstance().getUser().getSupplyStatus());
        storeUserEvent(userEvent);

        // Handle Community Events
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("stats");
        databaseReference.child("demandTotal").setValue(communityEvent.getDemandTotal());
        databaseReference.child("supplyTotal").setValue(communityEvent.getSupplyTotal());
        databaseReference.child("communityEvents").push().setValue(communityEvent);
    }

    @Override
    public void storeUserEvent(UserEvent userEvent) {
        // TODO handle timestamp
        // Store the unique event
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("stats/userEvents");
        databaseReference.push().setValue(userEvent);
    }
}
