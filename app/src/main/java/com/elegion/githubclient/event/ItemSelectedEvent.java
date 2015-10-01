package com.elegion.githubclient.event;

/**
 * @author Grigoriy Dzhanelidze
 */
public class ItemSelectedEvent {
    private Object mObject;

    public ItemSelectedEvent(Object object) {
        mObject = object;
    }

    public Object getObject() {
        return mObject;
    }
}
