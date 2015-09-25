package com.elegion.githubclient.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.elegion.githubclient.AppDelegate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author Artem Mochalov.
 */
public class ApiClient {

    public static final String STATUS_CODE = "status_code";
    public static final String GET_CURRENT_USER_URL = "https://api.github.com/user";
    public static final String GET_CURRENT_USER_REPOS_URL = "https://api.github.com/user/repos";
    public static final String CHANGE_TOKEN_URL = "https://github.com/login/oauth/access_token";
    public static final int STATUS_CODE_OK = 200;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String LIST_DATA_KEY = "list_data";
    private final OkHttpClient mClient = new OkHttpClient();
    private final Request.Builder mBuilder = new Request.Builder();
    private JSONObject mJsonData;
    private boolean mIsArray = false;

    public ApiClient setUrl(String url) {
        mBuilder.url(url);
        mBuilder.addHeader("User-Agent", "GitHub client App");
        mBuilder.addHeader("Accept", "application/json");
        return this;
    }

    public ApiClient setData(JSONObject jsonData) {
        mJsonData = jsonData;
        return this;
    }

    public ApiClient addAuthHeader() {
        mBuilder.addHeader("Authorization",
                "token " + AppDelegate.getSettings().getString(AppDelegate.ACCESS_TOKEN, null));
        return this;
    }

    public ApiClient asArray() {
        mIsArray = true;
        return this;
    }

    public JSONObject executePost() throws IOException, JSONException {
        RequestBody body = RequestBody.create(JSON, mJsonData.toString());
        Request request = mBuilder.post(body).build();
        return getJSONObject(request);
    }

    public JSONObject executeGet() throws IOException, JSONException {
        return getJSONObject(mBuilder.get().build());
    }

    @NonNull
    private JSONObject getJSONObject(Request request) throws IOException, JSONException {
        Response response = mClient.newCall(request).execute();
        JSONObject json;
        if (mIsArray) {
            json = new JSONObject();
            json.put(LIST_DATA_KEY, new JSONArray(response.body().string()));

        } else {
            json = new JSONObject(response.body().string());
        }

        json.put(STATUS_CODE, response.code());
        return json;
    }
}
