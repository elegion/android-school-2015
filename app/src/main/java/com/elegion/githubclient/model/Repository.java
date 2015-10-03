package com.elegion.githubclient.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.elegion.githubclient.BuildConfig;

/**
 * @author Artem Mochalov.
 */
public class Repository implements Parcelable {

    public static final Uri URI = new Uri.Builder()
            .scheme("content")
            .authority(BuildConfig.APPLICATION_ID)
            .path("repo")
            .build();

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };
    private String mName;

    public Repository(String name) {
        mName = name;
    }

    protected Repository(Parcel in) {
        mName = in.readString();
    }

    public String getName() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
    }
}
