package com.marshmallow.beacon.ui.marketing;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marshmallow.beacon.R;
import com.marshmallow.beacon.models.marketing.Sponsor;
import com.marshmallow.beacon.ui.BaseActivity;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SponsorsActivity extends BaseActivity {

    private RecyclerView sponsorsRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private SponsorsAdapter sponsorsAdapter;
    private Vector<Sponsor> sponsors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sponsors);
        activityType = MainActivityTypes.SPONSORS;
        super.onCreate(savedInstanceState);

        // TODO get sponsors
        sponsors = new Vector<>();
        Sponsor s1 = new Sponsor();
        s1.setName("RAM Brewery");
        s1.setUrl("https://www.theram.com/");
        Sponsor s2 = new Sponsor();
        s2.setName("Discover");
        s2.setUrl("https://www.discover.com/");
        sponsors.add(s1);
        sponsors.add(s2);
        sponsorsRecyclerView = findViewById(R.id.sponsors_recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        sponsorsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        sponsorsAdapter = new SponsorsAdapter(getApplicationContext(), sponsors);
        sponsorsRecyclerView.setAdapter(sponsorsAdapter);
    }

}
