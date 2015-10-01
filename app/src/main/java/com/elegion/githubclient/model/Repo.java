package com.elegion.githubclient.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Grigoriy Dzhanelidze
 */
public class Repo {
    @SerializedName("description")
    private String mDescription;

    public String getDescription() {
        return mDescription;
    }
}
