package com.elegion.githubclient.model;

/**
 * @author Artem Mochalov.
 */
public class Repository {

    private String mName;

    public Repository(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
