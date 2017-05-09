package com.tercalivre.blog;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tercalivre.blog.fragments.RecyclerViewFragment;

public class SearchActivity extends AppCompatActivity {

    public static final String QUERY = "SEARCH_ACTIVITY_QUERY";
    public static final String TAG = "SEARCH_ACTIVITY";

    private FragmentManager fragmentManager;

    private String mQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.super.onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mQuery = extras.getString(QUERY);
        }
        String title = getResources().getString(R.string.title_activity_search)+" \""+mQuery+"\"";
        //getSupportActionBar().setTitle(title);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecyclerViewFragment recyclerViewFragmentFragment = RecyclerViewFragment.newInstance(mQuery);
        fragmentTransaction.replace(R.id.container_search, recyclerViewFragmentFragment);
        fragmentTransaction.commit();


    }
}
