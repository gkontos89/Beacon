package com.marshmallow.beacon.backend;


import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.MarketingManager;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.marketing.Sponsor;
import com.marshmallow.beacon.models.marketing.SponsorVisitEvent;
import com.marshmallow.beacon.models.marketing.UserMarketDataSnapshot;
import com.marshmallow.beacon.models.user.User;

/**
 * This class implements Firebase backend communications
 *
 * Created by George on 4/29/2018.
 */
public class FirebaseBackend {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseInst;
    private DatabaseReference userReference = null;
    private ValueEventListener userValueEventListener = null;


    public FirebaseBackend() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInst = FirebaseDatabase.getInstance();
    }


    public void storeSponsorVisit(Sponsor sponsor) {
        final User user = UserManager.getInstance().getUser();
        final DatabaseReference sponsorReference = firebaseInst.getReference().child("sponsors").child(sponsor.getUid());
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot(user);
        SponsorVisitEvent sponsorVisitEvent = new SponsorVisitEvent(userMarketDataSnapshot);
        String sponsorVisitKey = sponsorReference.child("sponsorVisitEvents").push().getKey();
        if (sponsorVisitKey != null) {
            sponsorReference.child("sponsorVisitEvents").child(sponsorVisitKey).setValue(sponsorVisitEvent);
        }

        // Check is user has hit this sponsor already
        sponsorReference.child("usersVisited").equalTo(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Store that the user has visited the sponsor
                    sponsorReference.child("usersVisited").child(firebaseAuth.getUid()).setValue(true);

                    // Update the user's points
                    Integer gainedUserPoints = user.getPoints() + MarketingManager.getInstance().getUserSponsorMarketingValue(user);
                    DatabaseReference userReference = firebaseInst.getReference().child("users").child(firebaseAuth.getUid());
                    userReference.child("points").setValue(gainedUserPoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}
