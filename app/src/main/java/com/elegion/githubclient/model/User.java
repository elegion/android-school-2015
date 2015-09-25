package com.elegion.githubclient.model;

/**
 * @author Artem Mochalov.
 */
public class User {

    private final String mName;
    private final String mAvatarUrl;

    public User(String name, String avatarUrl) {
        mName = name;
        mAvatarUrl = avatarUrl;
    }

    public String getName() {
        return mName;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }
}
