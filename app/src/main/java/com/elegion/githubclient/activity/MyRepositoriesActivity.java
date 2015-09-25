package com.elegion.githubclient.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.elegion.githubclient.R;
import com.elegion.githubclient.adapter.RepositoriesAdapter;
import com.elegion.githubclient.api.ApiClient;
import com.elegion.githubclient.model.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Mochalov.
 */
public class MyRepositoriesActivity extends BaseActivity {

    private RecyclerView mRepositoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_repositories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRepositoryList = (RecyclerView) findViewById(R.id.repositories_list);
        mRepositoryList.setLayoutManager(new LinearLayoutManager(this));
        mRepositoryList.setAdapter(new RepositoriesAdapter());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        new GetRepositoriesTask().execute();
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

    private class GetRepositoriesTask extends AsyncTask<Void, Void, List<Repository>> {

        @Override
        protected List<Repository> doInBackground(Void... params) {
            List<Repository> repositories = new ArrayList<>();
            try {
                JSONObject responseObject = new ApiClient()
                        .addAuthHeader()
                        .setUrl(ApiClient.GET_CURRENT_USER_REPOS_URL)
                        .asArray()
                        .executeGet();

                if (responseObject.optInt(ApiClient.STATUS_CODE) != ApiClient.STATUS_CODE_OK) {
                    //TODO: handle error
                }

                JSONArray array = (JSONArray)responseObject.get(ApiClient.LIST_DATA_KEY);

                for (int i = 0; i < array.length(); i++) {
                    repositories.add(
                            new Repository(((JSONObject) array.get(i)).getString("name"))
                    );
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return repositories;
        }

        @Override
        protected void onPostExecute(List<Repository> repositories) {
            ((RepositoriesAdapter)mRepositoryList.getAdapter()).addAll(repositories);
        }
    }
}
