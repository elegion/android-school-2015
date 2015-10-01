package com.elegion.githubclient.model;

/**
 * @author Grigoriy Dzhanelidze
 */
public class Response<D> {
    private D mData;
    private Throwable mException;

    public D getData() {
        return mData;
    }

    public void setData(D data) {
        mData = data;
    }

    public Throwable getException() {
        return mException;
    }

    public void setException(Throwable exception) {
        mException = exception;
    }
}
