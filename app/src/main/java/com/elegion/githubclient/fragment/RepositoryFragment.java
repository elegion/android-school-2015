package com.elegion.githubclient.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elegion.githubclient.AppDelegate;
import com.elegion.githubclient.R;
import com.elegion.githubclient.event.ItemSelectedEvent;
import com.elegion.githubclient.loader.RepoLoader;
import com.elegion.githubclient.model.Repo;
import com.elegion.githubclient.model.Response;
import com.elegion.githubclient.utils.Otto;
import com.squareup.otto.Subscribe;

/**
 * @author Grigoriy Dzhanelidze
 */
public class RepositoryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Response<Repo>> {
    private static final String NAME = "name";

    private TextView mDescription;

    public static RepositoryFragment newInstance(String name) {
        RepositoryFragment fragment = new RepositoryFragment();
        if (name != null) {
            Bundle bundle = new Bundle();
            bundle.putString(NAME, name);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onItemSelected(ItemSelectedEvent event) {
        loadRepo(getArguments().getString(NAME));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_repository, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDescription = (TextView) view.findViewById(R.id.text);
        String name = null;
        if (getArguments() != null) {
            name = getArguments().getString(NAME);
        }
        if (!TextUtils.isEmpty(name)) {
            mDescription.setText("");
            loadRepo(name);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Otto.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Otto.unregister(this);
    }

    private void loadRepo(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(NAME, name);
        getLoaderManager().restartLoader(R.id.repo_loader, bundle, this);
    }

    @Override
    public Loader<Response<Repo>> onCreateLoader(int id, Bundle args) {
        if (getActivity() == null) {
            return null;
        }
        final String username = AppDelegate.getSettings().getString(AppDelegate.USER_NAME, "");
        final String repo = args.getString(NAME);
        return new RepoLoader(getActivity(), username, repo);
    }

    @Override
    public void onLoadFinished(Loader<Response<Repo>> loader, Response<Repo> data) {
        if (data.getData() != null) {
            mDescription.setText(data.getData().getDescription());
            getLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Response<Repo>> loader) {

    }
}
