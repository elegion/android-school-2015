package com.elegion.githubclient.model;

import android.net.Uri;

import com.elegion.githubclient.BuildConfig;
import com.google.gson.annotations.SerializedName;

/**
 * @author Grigoriy Dzhanelidze
 */
public class Repo {

    public static final Uri URI = new Uri.Builder()
            .scheme("content")
            .authority(BuildConfig.APPLICATION_ID)
            .path("repo")
            .build();

    @SerializedName("id")
    private long mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("description")
    private String mDescription;

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

}
