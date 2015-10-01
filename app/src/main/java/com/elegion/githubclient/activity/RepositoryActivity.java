package com.elegion.githubclient.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.elegion.githubclient.R;
import com.elegion.githubclient.fragment.RepositoryFragment;

public class RepositoryActivity extends AppCompatActivity {
    public static final String EXTRA_REPO = "repo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_repository);
        final String repo = getIntent().getStringExtra(EXTRA_REPO);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(repo);
        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.detail, RepositoryFragment.newInstance(repo))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
