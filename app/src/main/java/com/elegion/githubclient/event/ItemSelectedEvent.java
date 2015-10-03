package com.elegion.githubclient.event;

/**
 * @author Grigoriy Dzhanelidze
 */
public class ItemSelectedEvent {

    private final long mId;

    public ItemSelectedEvent(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

}
