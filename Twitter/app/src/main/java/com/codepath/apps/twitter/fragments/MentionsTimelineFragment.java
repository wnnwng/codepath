package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MentionsTimelineFragment extends TweetsListFragment {
    private final static String CATEGORY = "_mentions";
    private TwitterClient client;
    MenuItem pb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        setHasOptionsMenu(true);
    }

    protected void fetchTweets(long oldestId, final boolean clearData) {
        if (!loading) {
            pb.setVisible(true);
            loading = true;
            client.getMentionsTimeline(oldestId - 1, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    addAll(Tweet.fromJSONArray(response, CATEGORY), clearData);
                    setSwipeRefreshing(false);
                    pb.setVisible(false);
                    loading = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pb.setVisible(false);
                    setSwipeRefreshing(false);
                    loading = false;
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        pb = menu.findItem(R.id.miActionProgress);
        populateTimeline(-1, false, CATEGORY);
        super.onPrepareOptionsMenu(menu);
    }
}
