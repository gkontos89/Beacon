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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.ContactRequestManager;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.broadcasts.AddNewContactBroadcast;
import com.marshmallow.beacon.broadcasts.ContactUpdateBroadcast;
import com.marshmallow.beacon.broadcasts.CreateUserStatusBroadcast;
import com.marshmallow.beacon.broadcasts.LoadUserStatusBroadcast;
import com.marshmallow.beacon.broadcasts.RequestUpdateBroadcast;
import com.marshmallow.beacon.broadcasts.SignInStatusBroadcast;
import com.marshmallow.beacon.models.Contact;
import com.marshmallow.beacon.models.Request;
import com.marshmallow.beacon.models.Rolodex;
import com.marshmallow.beacon.models.User;

import java.util.HashMap;
import java.util.List;

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
    private Query requestsInQuery = null;
    private Query requestsOutQuery = null;
    private ValueEventListener userValueEventListener = null;
    private ValueEventListener statsSupplyTotalRefEventListener = null;
    private ValueEventListener statsDemandTotalRefEventListener = null;
    private ChildEventListener requestsInQueryListener = null;
    private ChildEventListener requestsOutQueryListener = null;
    private HashMap<Query, ChildEventListener> contactReferences = null;
    private Integer supplyTotal;
    private Integer demandTotal;

    public FirebaseBackend() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void initializeUserListeners(final Context context) {
        userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserManager.getInstance().setUser(dataSnapshot.getValue(User.class));
                if (requestsInQuery == null && requestsOutQuery == null) {
                    initializeRequestListeners(context);
                }

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
        };

        userReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid());
        userReference.addValueEventListener(userValueEventListener);
    }

    private void initializeRequestListeners(final Context context) {
        String username = UserManager.getInstance().getUser().getUsername();
        requestsInQuery = FirebaseDatabase.getInstance().getReference("requests").orderByChild("to").equalTo(username);
        requestsOutQuery = FirebaseDatabase.getInstance().getReference("requests").orderByChild("from").equalTo(username);

        requestsInQueryListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request requestIn = dataSnapshot.getValue(Request.class);
                ContactRequestManager.getInstance().addRequestIn(requestIn);
                context.sendBroadcast(new RequestUpdateBroadcast().getRequestUpdateBroadcast());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request requestIn = dataSnapshot.getValue(Request.class);
                ContactRequestManager.getInstance().addRequestIn(requestIn);
                context.sendBroadcast(new RequestUpdateBroadcast().getRequestUpdateBroadcast());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Request request = dataSnapshot.getValue(Request.class);
                ContactRequestManager.getInstance().removeRequestIn(request);
                context.sendBroadcast(new RequestUpdateBroadcast().getRequestUpdateBroadcast());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // TODO submit error
            }
        };

        requestsOutQueryListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request requestOut = dataSnapshot.getValue(Request.class);
                ContactRequestManager.getInstance().addRequestOut(requestOut);
                context.sendBroadcast(new RequestUpdateBroadcast().getRequestUpdateBroadcast());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request requestOut = dataSnapshot.getValue(Request.class);
                ContactRequestManager.getInstance().addRequestOut(requestOut);
                context.sendBroadcast(new RequestUpdateBroadcast().getRequestUpdateBroadcast());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Request requestOut = dataSnapshot.getValue(Request.class);
                ContactRequestManager.getInstance().removeRequestsOut(requestOut);
                context.sendBroadcast(new RequestUpdateBroadcast().getRequestUpdateBroadcast());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // submit error
            }
        };

        requestsInQuery.addChildEventListener(requestsInQueryListener);
        requestsOutQuery.addChildEventListener(requestsOutQueryListener);
    }

    private void initializeStatsListeners() {
        statsSupplyTotalRefEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supplyTotal = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        statsDemandTotalRefEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                demandTotal = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        statsSupplyTotalRef = FirebaseDatabase.getInstance().getReference("stats/supplyTotal");
        statsDemandTotalRef = FirebaseDatabase.getInstance().getReference("stats/demandTotal");
        statsSupplyTotalRef.addValueEventListener(statsSupplyTotalRefEventListener);
        statsDemandTotalRef.addValueEventListener(statsDemandTotalRefEventListener);
    }

    private void cleanupDatabaseReferences() {
        userReference.removeEventListener(userValueEventListener);
        statsSupplyTotalRef.removeEventListener(statsSupplyTotalRefEventListener);
        statsDemandTotalRef.removeEventListener(statsDemandTotalRefEventListener);

        userValueEventListener = null;
        statsSupplyTotalRefEventListener = null;
        statsDemandTotalRefEventListener = null;
        userReference = null;
        statsSupplyTotalRef = null;
        statsDemandTotalRef = null;

        removeContactListeners();
        removeRequestListeners();
    }

    private void removeRequestListeners() {
        requestsInQuery.removeEventListener(requestsInQueryListener);
        requestsOutQuery.removeEventListener(requestsOutQueryListener);
        requestsInQueryListener = null;
        requestsOutQueryListener = null;
        requestsInQuery = null;
        requestsOutQuery = null;
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
                            // Trim email
                            String username = email.replace("@gmail.com", "");
                            UserManager.getInstance().getUser().setUsername(username);
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
        cleanupDatabaseReferences();
        firebaseAuth.signOut();
    }

    private void storeNewUser(String username) {
        User user = new User(username);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseAuth.getUid()).setValue(user);
    }



    public void initializeContactListeners(final Context context) {
        contactReferences = new HashMap<>();
        if (UserManager.getInstance().getUser().getRolodex() != null) {
            List<String> usernames = UserManager.getInstance().getUser().getRolodex().getUsernames();
            for (String username : usernames) {
                Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(username);
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Contact contact = dataSnapshot.getValue(Contact.class);
                        ContactUpdateBroadcast contactUpdateBroadcast = new ContactUpdateBroadcast(contact.getUsername(),
                                contact.getDemandStatus(), contact.getSupplyStatus());
                        context.sendBroadcast(contactUpdateBroadcast.getBroadcastIntent());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Contact contact = dataSnapshot.getValue(Contact.class);
                        ContactUpdateBroadcast contactUpdateBroadcast = new ContactUpdateBroadcast(contact.getUsername(),
                                contact.getDemandStatus(), contact.getSupplyStatus());
                        context.sendBroadcast(contactUpdateBroadcast.getBroadcastIntent());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        // TODO removed? Likely just remove from contact list if they deleted profile
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // TODO failures?
                    }
                };

                query.addChildEventListener(childEventListener);
                contactReferences.put(query, childEventListener);
            }
        }
    }

    public void removeContactListeners() {
        if (contactReferences != null) {
            for (HashMap.Entry<Query, ChildEventListener> entry : contactReferences.entrySet()) {
                Query query = entry.getKey();
                ChildEventListener childEventListener = entry.getValue();
                query.removeEventListener(childEventListener);
                childEventListener = null;
                query = null;
            }

            contactReferences.clear();
        }
    }

    @Override
    public void sendNewContactRequest(final Context context, final Request request) {
        // First make sure the requested user exists
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").orderByChild("username").equalTo(request.getTo()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If user exists submit the request!
                if (dataSnapshot.exists()) {
                    // Create Request model and submit to database
                    DatabaseReference requestsReference = FirebaseDatabase.getInstance().getReference("requests");
                    String requestUniqueId = requestsReference.child("requests").push().getKey();
                    if (requestUniqueId != null) {
                        request.setUid(requestUniqueId);
                        requestsReference.child(requestUniqueId).setValue(request);
                        AddNewContactBroadcast addNewContactBroadcast = new AddNewContactBroadcast(null, null);
                        context.sendBroadcast(addNewContactBroadcast.getSuccessfulBroadcast());
                    } else {
                        AddNewContactBroadcast addNewContactBroadcast = new AddNewContactBroadcast("Request creation failed...", null);
                        context.sendBroadcast(addNewContactBroadcast.getRequestCreationFailedBroadcast());
                    }
                } else {
                    AddNewContactBroadcast addNewContactBroadcast = new AddNewContactBroadcast(null, null);
                    context.sendBroadcast(addNewContactBroadcast.getContactNotFoundBroadcast());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AddNewContactBroadcast addNewContactBroadcast = new AddNewContactBroadcast(databaseError.getMessage(), null);
                context.sendBroadcast(addNewContactBroadcast.getFailureBroadcast());
            }
        });
    }

    @Override
    public void acceptRequest(Request request) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        request.setStatus(Request.Status.ACCEPTED);
        databaseReference.child("requests").child(request.getUid()).setValue(request);
        Rolodex rolodex = UserManager.getInstance().getUser().getRolodex();
        if (rolodex == null) {
            rolodex = new Rolodex();
        }

        rolodex.addUsername(request.getFrom());
        databaseReference.child("users").child(firebaseAuth.getUid()).child("rolodex").setValue(rolodex);
    }

    @Override
    public void declineRequest(Request request) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        request.setStatus(Request.Status.DECLINED);
        databaseReference.child("requests").child(request.getUid()).setValue(request);
    }

    @Override
    public void confirmRequest(Request request) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("requests").child(request.getUid()).removeValue();
        Rolodex rolodex = UserManager.getInstance().getUser().getRolodex();
        if (rolodex == null) {
            rolodex = new Rolodex();
        }

        rolodex.addUsername(request.getTo());
        databaseReference.child("users").child(firebaseAuth.getUid()).child("rolodex").setValue(rolodex);
    }

    @Override
    public void clearRequest(Request request) {
        cancelRequest(request);
    }

    @Override
    public void cancelRequest(Request request) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("requests").child(request.getUid()).removeValue();
    }
}
