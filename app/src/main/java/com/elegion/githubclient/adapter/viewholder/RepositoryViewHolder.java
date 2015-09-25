package com.elegion.githubclient.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.elegion.githubclient.R;
import com.elegion.githubclient.model.Repository;

/**
 * @author Artem Mochalov.
 */
public class RepositoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView mRepositoryName;

    public RepositoryViewHolder(View itemView) {
        super(itemView);
        mRepositoryName = (TextView) itemView.findViewById(R.id.txt_name);
    }

    public void bindItem(Repository repository) {
        mRepositoryName.setText(repository.getName());
    }
}
