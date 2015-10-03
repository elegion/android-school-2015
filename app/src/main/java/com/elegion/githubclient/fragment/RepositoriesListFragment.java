package com.elegion.githubclient.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.githubclient.R;
import com.elegion.githubclient.adapter.RepositoriesAdapter;
import com.elegion.githubclient.api.ApiClient;
import com.elegion.githubclient.api.GithubApi;
import com.elegion.githubclient.model.Repo;
import com.elegion.githubclient.model.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Grigoriy Dzhanelidze
 */
public class RepositoriesListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LIST = "list";

    private RecyclerView mRepositoryList;

    private RepositoriesAdapter mAdapter = new RepositoriesAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_repositories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRepositoryList = (RecyclerView) view.findViewById(R.id.repositories_list);
        mAdapter.setHasStableIds(true);
        mRepositoryList.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(R.id.repositories_list, Bundle.EMPTY, this);
        GithubApi.getService().getRepos(new ReposCallback(getActivity().getContentResolver()));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity().getApplicationContext(), Repo.URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e("Repos", DatabaseUtils.dumpCursorToString(data));
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private static class ReposCallback implements Callback<List<Repo>> {

        private final ContentResolver mResolver;

        public ReposCallback(ContentResolver resolver) {
            mResolver = resolver;
        }

        @Override
        public void success(List<Repo> repos, Response response) {
            final ContentValues[] bulkValues = new ContentValues[repos.size()];
            for (int i = 0; i < bulkValues.length; ++i) {
                final Repo repo = repos.get(i);
                final ContentValues values = new ContentValues();
                values.put("_id", repo.getId());
                values.put("name", repo.getName());
                values.put("description", repo.getDescription());
                bulkValues[i] = values;
            }
            mResolver.bulkInsert(Repo.URI, bulkValues);
        }

        @Override
        public void failure(RetrofitError error) {

        }
    }

    /*private class GetRepositoriesTask extends AsyncTask<Void, Void, List<Repository>> {
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

                JSONArray array = (JSONArray) responseObject.get(ApiClient.LIST_DATA_KEY);

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
            ((RepositoriesAdapter) mRepositoryList.getAdapter()).addAll(repositories);
        }
    }*/

}
