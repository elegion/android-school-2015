package com.elegion.githubclient.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.elegion.githubclient.api.GithubApi;
import com.elegion.githubclient.model.Repo;
import com.elegion.githubclient.model.Response;

/**
 * @author Grigoriy Dzhanelidze
 */
public class RepoLoader extends AsyncTaskLoader<Response<Repo>> {
    private String mUsername;
    private String mRepo;

    private Response<Repo> mCachedResponse;

    public RepoLoader(Context context, String username, String repo) {
        super(context);
        mUsername = username;
        mRepo = repo;
    }

    @Override
    public Response<Repo> loadInBackground() {
        Response<Repo> response = new Response<>();
        try {
            Repo repo = GithubApi.getService().getRepo(mUsername, mRepo);
            response.setData(repo);
        } catch (Exception e) {
            response.setException(e);
        }
        mCachedResponse = response;
        return response;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mCachedResponse != null) {
            deliverResult(mCachedResponse);
        }
        if (takeContentChanged() || mCachedResponse == null) {
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        mCachedResponse = null;
    }
}
