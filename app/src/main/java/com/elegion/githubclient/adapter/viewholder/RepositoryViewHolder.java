package com.elegion.githubclient.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.elegion.githubclient.R;
import com.elegion.githubclient.event.ItemSelectedEvent;
import com.elegion.githubclient.model.Repository;
import com.elegion.githubclient.utils.Otto;

/**
 * @author Artem Mochalov.
 */
public class RepositoryViewHolder extends RecyclerView.ViewHolder {
    private final View mItemView;
    private final TextView mRepositoryName;

    public RepositoryViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mRepositoryName = (TextView) itemView.findViewById(R.id.txt_name);
    }

    public void bindItem(final Repository repository) {
        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Otto.post(new ItemSelectedEvent(repository.getName()));
            }
        });
        mRepositoryName.setText(repository.getName());
    }
}
