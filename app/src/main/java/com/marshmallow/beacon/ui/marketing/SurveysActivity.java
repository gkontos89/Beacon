package com.marshmallow.beacon.ui.marketing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.models.marketing.Survey;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SurveysActivity extends AppCompatActivity{

    private RecyclerView surveysRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SurveysAdapter surveysAdapter;
    private Vector<Survey> surveys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_surveys);
        super.onCreate(savedInstanceState);

        surveys = new Vector<>();
        surveysRecyclerView = findViewById(R.id.surveys_rv);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        surveysRecyclerView.setLayoutManager(recyclerViewLayoutManager);
//        surveysAdapter = new SurveysAdapter();
        surveysRecyclerView.setAdapter(surveysAdapter);
        // TODO where do we get surveys from?
    }

}
