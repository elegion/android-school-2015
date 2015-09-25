package com.elegion.githubclient.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.elegion.githubclient.AppDelegate;
import com.elegion.githubclient.R;
import com.elegion.githubclient.api.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * @author Artem Mochalov.
 */
public class LoginActivity extends BaseActivity {

    private static final String SOME_ERROR = "SOME_ERROR";
    public static final String ACCESS_TOKEN_RESPONSE_KEY = "access_token";
    private boolean mDataSend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new AuthWebViewClient());
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(buildAuthUrl());
    }

    private class AuthWebViewClient extends WebViewClient {

        private boolean checkDone(WebView view, String url) {
            boolean result = false;
            if (url.startsWith(getString(R.string.redirect_url)) && url.contains("code=")) {
                view.stopLoading();
                if (!mDataSend) {
                    changeCodeToAuthToken(getCodeFromUrl(url));
                    mDataSend = true;
                }
                result = true;
            }

            return result;
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return checkDone(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (isErrorUrl(url)) {
                view.stopLoading();
            } else if (!checkDone(view, url)) {
                super.onPageStarted(view, url, favicon);
            }
        }

        private boolean isErrorUrl(String url) {
            return url.contains("error");
        }
    }


    private String getCodeFromUrl(String url) {
        final int codeStart = url.indexOf("code=") + "code=".length();
        final int codeEnd = url.indexOf('&', codeStart) == -1 ? url.length() : url.indexOf('&', codeStart);
        return url.substring(codeStart, codeEnd);
    }

    private void changeCodeToAuthToken(String code) {
        ChangeCodeToAuthTokenTask task = new ChangeCodeToAuthTokenTask();
        task.execute(code);
    }

    private String buildAuthUrl() {
        return "https://github.com/login/oauth/authorize?client_id="
                + getString(R.string.client_id)
                + "&"
                + "redirect_uri="
                + getString(R.string.redirect_url)
                + "&"
                + "scope="
                + getString(R.string.scope);
    }

    private class ChangeCodeToAuthTokenTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            if (params.length == 0 || TextUtils.isEmpty(params[0])) {
                return false;
            }
            try {

                JSONObject requestJson = new JSONObject();
                requestJson.put("client_id", getString(R.string.client_id));
                requestJson.put("client_secret", getString(R.string.client_secret));
                requestJson.put("code", params[0]);

                JSONObject responseObject = new ApiClient()
                        .setData(requestJson)
                        .setUrl(ApiClient.CHANGE_TOKEN_URL)
                        .executePost();

                if (responseObject.optInt(ApiClient.STATUS_CODE) != ApiClient.STATUS_CODE_OK) {
                    //TODO: handle error
                }

                String accessToken = responseObject.optString(ACCESS_TOKEN_RESPONSE_KEY);

                if (!TextUtils.isEmpty(accessToken)) {
                    AppDelegate.getSettings()
                            .edit()
                            .putString(AppDelegate.ACCESS_TOKEN, accessToken)
                            .commit();

                    return true;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                startActivity(UserActivity.class, true);
            } else {
                showSingleToast(SOME_ERROR);
            }
        }
    }
}
