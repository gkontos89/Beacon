package com.marshmallow.beacon.ui.marketing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.models.marketing.Survey;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SurveysAdapter extends RecyclerView.Adapter<SurveysAdapter.SurveyHolder> {

    private Context context;
    private Vector<Survey> surveys;

    public SurveysAdapter(Context context, Vector<Survey> surveys) {
        this.context = context;
        this.surveys = surveys;
    }

    @NonNull
    @Override
    public SurveyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_basic, parent, false);
        return new SurveyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SurveyHolder holder, final int position) {
//        final Sponsor sponsor = sponsors.get(position);
//        holder.sponsorImage.setImageBitmap(sponsor.getProfilePictureBitmap());
//        holder.sponsorName.setText(sponsor.getName());
    }

    @Override
    public int getItemCount() { return surveys.size(); }

    public class SurveyHolder extends RecyclerView.ViewHolder {

        public ImageView surveyImage;
        public TextView surveyName;

        public SurveyHolder(View v) {
            super(v);
            surveyImage = v.findViewById(R.id.survey_image);
            surveyName = v.findViewById(R.id.sponsor_name);
        }
    }
}
