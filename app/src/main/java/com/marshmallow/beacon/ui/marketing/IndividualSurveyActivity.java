package com.marshmallow.beacon.ui.marketing;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.SurveyManager;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.marketing.SurveyItem;
import com.marshmallow.beacon.models.marketing.SurveyResult;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class IndividualSurveyActivity extends AppCompatActivity {

    // GUI handles
    private RecyclerView surveyRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private IndividualSurveyAdapter individualSurveyAdapter;
    private Vector<SurveyItem> surveyItems;
    private Button submitSurveyButton;
    private LinearLayout progressBarLayout;

    // Model
    private SurveyManager surveyManager;

    // Firebase
    private FirebaseDatabase firebaseInst;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_survey);

        firebaseInst = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        surveyManager = SurveyManager.getInstance();

        surveyItems = SurveyManager.getInstance().getCurrentSurvey().getSurveyItems();
        surveyRecyclerView = findViewById(R.id.individual_survey_recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        surveyRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        individualSurveyAdapter = new IndividualSurveyAdapter(surveyItems);
        surveyRecyclerView.setAdapter(individualSurveyAdapter);
        progressBarLayout = findViewById(R.id.survey_progress_bar_layout);

        submitSurveyButton = findViewById(R.id.submit_survey_button);
        submitSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Post pop-up and store survey result
                SurveyResult surveyResult = surveyManager.generateSurveyResult();
                if (surveyResult != null) {
                    showSubmittingWheel();
                    // Store survey results
                    DatabaseReference responseReference = firebaseInst
                            .getReference("sponsorSurveys")
                            .child(surveyManager.getCurrentSurvey().getSponsorUid())
                            .child("surveys")
                            .child(surveyManager.getCurrentSurvey().getSurveyUid())
                            .child("responses");
                    String newResponseUid = responseReference.push().getKey();
                    if (newResponseUid != null) {
                        responseReference.child(newResponseUid).setValue(surveyResult);
                    }

                    // Update the users points
                    int existingPoints = UserManager.getInstance().getUser().getPoints();
                    int newPointTotal = existingPoints + surveyManager.getCurrentSurvey().getPointReward();
                    DatabaseReference userPointReference = firebaseInst.getReference("users")
                            .child(firebaseAuth.getUid())
                            .child("points");
                    userPointReference.setValue(newPointTotal);
                    hideSubmittingWheel();
                    showSuccessfulSubmit(surveyManager.getCurrentSurvey().getPointReward());
                } else {
                    Toast.makeText(IndividualSurveyActivity.this, "Please answer all survey questions", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showSubmittingWheel() {
        surveyRecyclerView.setVisibility(View.INVISIBLE);
        submitSurveyButton.setVisibility(View.INVISIBLE);
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideSubmittingWheel() {
        progressBarLayout.setVisibility(View.GONE);
    }

    private void showSuccessfulSubmit(int pointsEarned) {
        // Show pop up window
        LayoutInflater inflater = (LayoutInflater) IndividualSurveyActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.survey_submit_pop_up, null);
        ((TextView)layout.findViewById(R.id.pop_point_total)).setText(String.format("%d Points", pointsEarned)); // TODO get like/dislike market values
        final PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(findViewById(R.id.activity_individual_survey_layout), Gravity.CENTER, 0, 0);

        // Dim the background
        final View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }

        Context context = popupWindow.getContentView().getContext();
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.75f;
        windowManager.updateViewLayout(container, layoutParams);

        (layout.findViewById(R.id.pop_up_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Un-dim the background
                layoutParams.dimAmount = 0.0f;
                windowManager.updateViewLayout(container, layoutParams);
                popupWindow.dismiss();
                finish();
            }
        });
    }

    private class IndividualSurveyAdapter extends RecyclerView.Adapter<IndividualSurveyAdapter.IndividualSurveyHolder> {
        private Vector<SurveyItem> surveyItems;

        public IndividualSurveyAdapter(Vector<SurveyItem> surveyItems) {
            this.surveyItems = surveyItems;
        }

        @NonNull
        @Override
        public IndividualSurveyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
            return new IndividualSurveyHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final IndividualSurveyAdapter.IndividualSurveyHolder holder, final int position) {
            holder.setSurveyItem(surveyItems.get(position));
            holder.surveyQuestion.setText(holder.surveyItem.getQuestion());
            for (String option : holder.surveyItem.getOptions()) {
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(option);
                holder.questionOptions.addView(radioButton);
            }

            holder.questionOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton checkedRadioButton = findViewById(checkedId);
                    String answer = checkedRadioButton.getText().toString();
                    surveyManager.setSurveyResponseItemAnswer(holder.surveyItem, answer);
                }
            });
        }

        @Override
        public int getItemCount() { return surveyItems.size(); }


        public class IndividualSurveyHolder extends RecyclerView.ViewHolder {
            public SurveyItem surveyItem;
            public TextView surveyQuestion;
            public RadioGroup questionOptions;

            public IndividualSurveyHolder(View v) {
                super(v);
                surveyQuestion = findViewById(R.id.survey_question);
                questionOptions = findViewById(R.id.radio_options);
            }

            public void setSurveyItem(SurveyItem surveyItem) {
                this.surveyItem = surveyItem;
            }
        }
    }
}
