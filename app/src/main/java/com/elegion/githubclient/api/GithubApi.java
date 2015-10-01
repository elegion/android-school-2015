package com.elegion.githubclient.api;

import com.elegion.githubclient.AppDelegate;
import com.elegion.githubclient.model.Repo;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * @author Grigoriy Dzhanelidze
 */
public class GithubApi {
    private static final GithubService SERVICE = new RestAdapter.Builder()
            .setEndpoint("https://api.github.com/")
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("User-Agent", "GitHub client App");
                    request.addHeader("Accept", "application/json");
                    request.addHeader("Authorization",
                            "token " + AppDelegate.getSettings().getString(AppDelegate.ACCESS_TOKEN, null));
                }
            })
            .setClient(new OkClient())
            .build()
            .create(GithubService.class);

    public static GithubService getService() {
        return SERVICE;
    }

    public interface GithubService {
        @GET("/repos/{user}/{repo}")
        Repo getRepo(@Path("user") String user, @Path("repo") String repo);
    }
}
