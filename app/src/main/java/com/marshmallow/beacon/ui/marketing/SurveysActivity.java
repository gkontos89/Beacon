package com.marshmallow.beacon.ui.marketing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.SurveyManager;
import com.marshmallow.beacon.models.marketing.DistributedSurvey;
import com.marshmallow.beacon.models.marketing.Sponsor;
import com.marshmallow.beacon.models.marketing.Survey;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SurveysActivity extends AppCompatActivity{

    // GUI handles
    private RecyclerView surveysRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SurveysAdapter surveysAdapter;

    // Models
    private Vector<SurveyController> surveyControllers;

    // Firebase
    private FirebaseDatabase firebaseInst;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference distributedSurveysReference;
    private ChildEventListener distributedSurveysListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_surveys);
        super.onCreate(savedInstanceState);

        surveyControllers = new Vector<>();
        surveysRecyclerView = findViewById(R.id.surveys_rv);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        surveysRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        surveysAdapter = new SurveysAdapter(surveyControllers);
        surveysRecyclerView.setAdapter(surveysAdapter);

        firebaseInst = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeDistributedSurveyListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        destroyDistributedSurveyListeners();
    }

    private void initializeDistributedSurveyListeners() {
        distributedSurveysReference = firebaseInst.getReference("distributedSurveys").child(firebaseAuth.getUid());
        distributedSurveysListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                surveyControllers.add(new SurveyController((DistributedSurvey) dataSnapshot.getValue()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        distributedSurveysReference.addChildEventListener(distributedSurveysListener);
    }

    private void destroyDistributedSurveyListeners() {
        distributedSurveysReference.removeEventListener(distributedSurveysListener);
        distributedSurveysListener = null;
        distributedSurveysReference = null;
    }

    private class SurveyController {

        // Models
        private DistributedSurvey distributedSurvey;
        private Survey survey;
        private Sponsor sponsor;
        private boolean surveyLoaded;
        private boolean sponsorLoaded;

        // Firebase
        private DatabaseReference surveyObjectReference;
        private DatabaseReference sponsorReference;

        public SurveyController(DistributedSurvey distributedSurvey) {
            this.distributedSurvey = distributedSurvey;
            surveyLoaded = false;
            sponsorLoaded = false;
            initializeSurveyControllerListeners();
        }

        public Survey getSurvey() {
            return survey;
        }

        public Sponsor getSponsor() {
            return sponsor;
        }

        private void initializeSurveyControllerListeners() {
            surveyObjectReference = firebaseInst.getReference("sponsorSurveys").child(distributedSurvey.getSponsorUid()).child("surveys").child(distributedSurvey.getSurveyUid());
            surveyObjectReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    survey = (Survey) dataSnapshot.getValue();
                    surveyLoaded = true;
                    checkIfLoadingIsComplete();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            sponsorReference = firebaseInst.getReference("sponsors").child(distributedSurvey.getSponsorUid());
            sponsorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sponsor = (Sponsor) dataSnapshot.getValue();
                    sponsorLoaded = true;
                    checkIfLoadingIsComplete();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void checkIfLoadingIsComplete() {
            if (sponsorLoaded && surveyLoaded) {
                surveysAdapter.notifyDataSetChanged();
            }
        }
    }

    private class SurveysAdapter extends RecyclerView.Adapter<SurveysAdapter.SurveyHolder> {

        private Vector<SurveyController> surveyControllers;

        public SurveysAdapter(Vector<SurveyController> surveyControllers) {
            this.surveyControllers = surveyControllers;
        }

        @NonNull
        @Override
        public SurveyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_basic, parent, false);
            return new SurveyHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final SurveysAdapter.SurveyHolder holder, final int position) {
            holder.setSponsor(surveyControllers.get(position).getSponsor());
            holder.setSurvey(surveyControllers.get(position).getSurvey());
            holder.surveyTitle.setText(holder.getSurvey().getSurveyTitle());
            holder.pointsAwardedText.setText(String.format("Points awarded:  %d", holder.getSurvey().getPointReward()));
            holder.sponsoredByText.setText(String.format("Sponsored by %s", holder.getSponsor().getName()));
            // TODO handle sponsor image from database.  Damn the hardcoding!!!
            switch (holder.getSponsor().getName()) {
                case "At Work Sports Bar":
                    holder.surveySponsorImage.setImageResource(R.drawable.at_work_sports_bar_and_grill);
                    break;

                case "Brick and Motor Botique":
                    holder.surveySponsorImage.setImageResource(R.drawable.brick_and_motor_botique);
                    break;

                case "A Jacq of All Trades":
                    holder.surveySponsorImage.setImageResource(R.drawable.jacq_of_all_trades);
                    break;

                case "Make It Train":
                    holder.surveySponsorImage.setImageResource(R.drawable.make_it_train);
                    break;

                case "Rhodes Capital Management":
                    holder.surveySponsorImage.setImageResource(R.drawable.rhodes_capital_management);
                    break;
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SurveyManager.getInstance().setCurrentSurvey(holder.getSurvey());
                    Intent intent = new Intent(getApplicationContext(), IndividualSurveyActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() { return surveyControllers.size(); }

        public class SurveyHolder extends RecyclerView.ViewHolder {

            public Survey survey;
            public Sponsor sponsor;
            public TextView surveyTitle;
            public TextView pointsAwardedText;
            public ImageView surveySponsorImage;
            public TextView sponsoredByText;


            public SurveyHolder(View v) {
                super(v);
                surveyTitle = findViewById(R.id.survey_title);
                pointsAwardedText = findViewById(R.id.points_awarded_text);
                surveySponsorImage = findViewById(R.id.survey_image);
                sponsoredByText = findViewById(R.id.sponsored_by_text);
            }

            public Survey getSurvey() {
                return survey;
            }

            public void setSurvey(Survey survey) {
                this.survey = survey;
            }


            public Sponsor getSponsor() {
                return sponsor;
            }

            public void setSponsor(Sponsor sponsor) {
                this.sponsor = sponsor;
            }
        }
    }

}
