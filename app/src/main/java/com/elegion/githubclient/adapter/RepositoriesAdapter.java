package com.elegion.githubclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.githubclient.R;
import com.elegion.githubclient.adapter.viewholder.RepositoryViewHolder;
import com.elegion.githubclient.model.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Mochalov.
 */
public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoryViewHolder> {

    private final List<Repository> mRepositoryList = new ArrayList<>();

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_repository, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        holder.bindItem(mRepositoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return mRepositoryList.size();
    }

    public void addAll(List<Repository> repositories) {
        mRepositoryList.addAll(repositories);
        notifyItemRangeInserted(0, repositories.size());
    }
}
