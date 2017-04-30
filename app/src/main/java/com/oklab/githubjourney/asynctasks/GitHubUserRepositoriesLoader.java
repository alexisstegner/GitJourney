package com.oklab.githubjourney.asynctasks;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.oklab.githubjourney.R;
import com.oklab.githubjourney.data.HTTPConnectionResult;
import com.oklab.githubjourney.data.ReposDataEntry;
import com.oklab.githubjourney.parsers.ReposParser;
import com.oklab.githubjourney.services.FetchHTTPConnectionService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by olgakuklina on 2017-04-01.
 */

public class GitHubUserRepositoriesLoader extends AsyncTaskLoader<List<ReposDataEntry>> {
    private static final String TAG = GitHubUserRepositoriesLoader.class.getSimpleName();
    private final int page;
    private final String userName;

    public GitHubUserRepositoriesLoader(Context context, int page, String userName) {
        super(context);
        this.page = page;
        this.userName = userName;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<ReposDataEntry> loadInBackground() {
        String uri = getContext().getString(R.string.url_user_repos, page, userName);
        FetchHTTPConnectionService fetchHTTPConnectionService = new FetchHTTPConnectionService(uri, getContext());
        HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "result = " + result.getResult());

        try {
            JSONArray jsonArray = new JSONArray(result.getResult());
            return new ReposParser().parse(jsonArray);

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }
}