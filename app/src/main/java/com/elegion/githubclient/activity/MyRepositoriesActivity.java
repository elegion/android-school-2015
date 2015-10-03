package com.elegion.githubclient.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.elegion.githubclient.R;
import com.elegion.githubclient.event.ItemSelectedEvent;
import com.elegion.githubclient.utils.ActivityBuilder;
import com.elegion.githubclient.utils.Otto;
import com.squareup.otto.Subscribe;

/**
 * @author Artem Mochalov.
 */
public class MyRepositoriesActivity extends BaseActivity {
    @SuppressWarnings("unused")
    @Subscribe
    public void onItemSelected(ItemSelectedEvent event) {
        new ActivityBuilder()
                .setContext(this)
                .setClass(RepositoryActivity.class)
                .putExtra(RepositoryActivity.EXTRA_REPO, event.getId())
                .startActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_repositories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (findViewById(R.id.detail) == null) {
            Otto.register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (findViewById(R.id.detail) == null) {
            Otto.unregister(this);
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
