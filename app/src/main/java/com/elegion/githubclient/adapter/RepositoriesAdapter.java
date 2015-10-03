package com.elegion.githubclient.adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
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

    private Cursor mCursor;

    public Cursor swapCursor(@NonNull Cursor cursor) {
        final Cursor oldCursor = mCursor;
        mCursor = cursor;
        notifyDataSetChanged();
        return oldCursor;
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_repository, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        if(mCursor.moveToPosition(position)) {
            holder.bindItem(mCursor);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    @Override
    public long getItemId(int position) {
        if(mCursor.moveToPosition(position)) {
            return mCursor.getLong(mCursor.getColumnIndex("_id"));
        }
        return super.getItemId(position);
    }
}
