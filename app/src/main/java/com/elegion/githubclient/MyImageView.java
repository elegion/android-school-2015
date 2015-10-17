package com.elegion.githubclient;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {
    public final boolean RoundCrop;
    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyImageView, 0, 0);
        RoundCrop = a.getBoolean(R.styleable.MyImageView_round_crop, false);
        a.recycle();
    }
}