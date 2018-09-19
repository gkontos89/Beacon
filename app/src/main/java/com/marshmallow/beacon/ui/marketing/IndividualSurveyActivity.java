package com.marshmallow.beacon.ui.marketing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.SurveyManager;
import com.marshmallow.beacon.models.marketing.Survey;
import com.marshmallow.beacon.models.marketing.SurveyItem;

import org.w3c.dom.Text;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class IndividualSurveyActivity extends AppCompatActivity{

    // GUI handles
    private RecyclerView surveyRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private IndividualSurveyAdapter individualSurveyAdapter;
    private Vector<SurveyItem> surveyItems;

    // Firebase
    private FirebaseDatabase firebaseInst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_survey);

        surveyItems = SurveyManager.getInstance().getCurrentSurvey().getSurveyItems();
        surveyRecyclerView = findViewById(R.id.indvidual_survey_recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        surveyRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        individualSurveyAdapter = new IndividualSurveyAdapter(surveyItems);
        surveyRecyclerView.setAdapter(individualSurveyAdapter);
    }

    private class IndividualSurveyAdapter extends RecyclerView.Adapter<IndividualSurveyAdapter.IndividualSurveyHolder> {
        private Vector<SurveyItem> surveyItems;

        public IndividualSurveyAdapter(Vector<SurveyItem> surveyItems) {
            this.surveyItems = surveyItems;
        }

        @NonNull
        @Override
        public IndividualSurveyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_basic, parent, false);
            return new IndividualSurveyHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final IndividualSurveyAdapter.IndividualSurveyHolder holder, final int position) {
            SurveyItem surveyItem = surveyItems.get(position);
            holder.surveyQuestion.setText(surveyItem.getQuestion());
            for (String option : surveyItem.getOptions()) {
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(option);
                holder.questionOptions.addView(radioButton);
            }
        }

        @Override
        public int getItemCount() { return surveyItems.size(); }


        public class IndividualSurveyHolder extends  RecyclerView.ViewHolder {

            public TextView surveyQuestion;
            public RadioGroup questionOptions;

            public IndividualSurveyHolder(View v) {
                super(v);
                surveyQuestion = findViewById(R.id.survey_question);
                questionOptions = findViewById(R.id.radio_options);
            }
        }
    }
}
