package com.elegion.githubclient.activity;


import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.elegion.githubclient.AppDelegate;
import com.elegion.githubclient.utils.ActivityBuilder;

/**
 * @author Artem Mochalov.
 */
public class BaseActivity extends AppCompatActivity {

    private Toast mToast;

    protected void startActivity(Class<? extends BaseActivity> clazz, boolean isClearTask) {
        ActivityBuilder builder = new ActivityBuilder()
                .setClass(clazz)
                .setContext(this);

        if (isClearTask) {
            builder.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        builder.startActivity();
    }

    protected void showSingleToast(String text) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    protected void logout() {
        removeCookies();
        AppDelegate.getSettings().edit().remove(AppDelegate.ACCESS_TOKEN).commit();
        startActivity(LoginActivity.class, true);
    }

    private void removeCookies() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
            CookieManager.getInstance().removeAllCookie();
        } else {
            CookieManager.getInstance().removeAllCookies(null);
        }
    }
}
