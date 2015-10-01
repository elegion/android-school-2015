package com.elegion.githubclient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Mochalov.
 */
public final class ActivityBuilder {

    //region ---------------------------------------- Variables

    private final Bundle mExtras = new Bundle();
    private Context mContext;
    private Class<? extends Activity> mClass;
    private Uri mData;
    private int mFlags;
    private String mAction;
    private boolean mFinishCallerActivity;
    private int mRequestCode;

    //endregion

    //region ---------------------------------------- Public methods

    public ActivityBuilder setContext(Context context) {
        mContext = context;
        return this;
    }

    public ActivityBuilder setClass(Class<? extends Activity> clazz) {
        mClass = clazz;
        return this;
    }

    public ActivityBuilder setAction(String action) {
        mAction = action;
        return this;
    }

    public ActivityBuilder setData(Uri data) {
        mData = data;
        return this;
    }

    public ActivityBuilder addFlags(int flags) {
        mFlags = flags;
        return this;
    }

    public ActivityBuilder putExtra(String name, String value) {
        mExtras.putString(name, value);
        return this;
    }

    public ActivityBuilder putExtra(String name, int value) {
        mExtras.putInt(name, value);
        return this;
    }

    public ActivityBuilder putExtra(String name, long value) {
        mExtras.putLong(name, value);
        return this;
    }

    public ActivityBuilder putExtra(String name, boolean value) {
        mExtras.putBoolean(name, value);
        return this;
    }

    public ActivityBuilder putExtra(String key, Parcelable value) {
        mExtras.putParcelable(key, value);
        return this;
    }

    public ActivityBuilder putExtra(String key, List<? extends Parcelable> value) {
        mExtras.putParcelableArrayList(key, new ArrayList<>(value));
        return this;
    }

    public ActivityBuilder putExtra(String key, Serializable value) {
        mExtras.putSerializable(key, value);
        return this;
    }

    public ActivityBuilder setFinishCallerActivity() {
        mFinishCallerActivity = true;
        return this;
    }

    public ActivityBuilder setRequestCode(int code) {
        mRequestCode = code;
        return this;
    }

    public void startActivityForResult() {
        Intent intent = createBaseStartActivityIntent();
        if (intent != null) {
            ((Activity) mContext).startActivityForResult(intent, mRequestCode);
        }
    }

    public void startActivity() {
        Intent intent = createBaseStartActivityIntent();
        if (intent != null) {
            mContext.startActivity(intent);
            if (mContext instanceof Activity && mFinishCallerActivity) {
                ((Activity) mContext).finish();
            }
        }
    }

    //endregion

    //region ---------------------------------------- Private methods

    private Intent createBaseStartActivityIntent() {
        Intent intent = null;
        if (mContext != null) {
            if (mClass != null) {
                intent = new Intent(mContext, mClass);
            } else if (!TextUtils.isEmpty(mAction)) {
                intent = new Intent(mAction);
            } else {
                throw new IllegalArgumentException("Action or target class should be set");
            }

            intent.addFlags(mFlags);
            intent.putExtras(mExtras);

            if (mData != null) {
                intent.setData(mData);
            }
        }
        return intent;
    }

    //endregion
}

