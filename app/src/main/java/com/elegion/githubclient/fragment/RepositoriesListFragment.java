package com.elegion.githubclient.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * @author Grigoriy Dzhanelidze
 */
public class RepositoriesListFragment extends Fragment {
    private static final String LIST = "list";
    private RecyclerView mRepositoryList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_repositories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRepositoryList = (RecyclerView) view.findViewById(R.id.repositories_list);
        mRepositoryList.setAdapter(new RepositoriesAdapter());
        if (savedInstanceState == null) {
            new GetRepositoriesTask().execute();
        } else {
            ArrayList<Parcelable> parcelables = savedInstanceState.getParcelableArrayList(LIST);
            if (parcelables == null) {
                return;
            }
            ArrayList<Repository> repositories = new ArrayList<>(parcelables.size());
            for (Parcelable p : parcelables) {
                repositories.add((Repository) p);
            }
            ((RepositoriesAdapter) mRepositoryList.getAdapter()).addAll(repositories);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST,
                ((RepositoriesAdapter) mRepositoryList.getAdapter()).getRepositoryList());
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
    }
}
