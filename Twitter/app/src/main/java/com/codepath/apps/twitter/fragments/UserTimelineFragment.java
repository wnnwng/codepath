package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserTimelineFragment extends TweetsListFragment {
    private static final String CATEGORY = "_user_";
    private TwitterClient client;
    MenuItem pb;

    public static UserTimelineFragment newInstance(String username) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        setHasOptionsMenu(true);
    }

    protected void fetchTweets(long oldestTweetId, final boolean clearData) {
        if (!loading) {
            loading = true;
            final String username = getArguments().getString("username");
            pb.setVisible(true);
            client.getUserTimeline(oldestTweetId, username, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    addAll(Tweet.fromJSONArray(response, CATEGORY + username), clearData);
                    setSwipeRefreshing(false);
                    pb.setVisible(false);
                    loading = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    setSwipeRefreshing(false);
                    pb.setVisible(false);
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
        populateTimeline(-1, false, CATEGORY + getArguments().get("username"));
        super.onPrepareOptionsMenu(menu);
    }
}
