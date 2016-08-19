package com.codepath.apps.twitter.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchResultsFragment extends TweetsListFragment {
    private TwitterClient client;
    private String query;
    MenuItem pb;

    public static SearchResultsFragment newInstance() {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        searchResultsFragment.setArguments(args);
        searchResultsFragment.query = "";
        return searchResultsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        setHasOptionsMenu(true);
        query = "";
    }

    @Override
    protected void fetchTweets(long oldestId, boolean clearData) {
        if (query != null && query.length() > 0){
            client.search(query, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        addAll(Tweet.fromJSONArray(response.getJSONArray("statuses"), null), false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    hideKeyboard();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    hideKeyboard();
                }
            });
        }
    }

    private void hideKeyboard() {
        Activity activity = getActivity();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public void startQuery(String newQuery) {
        query = newQuery;
        fetchTweets(-1, true);

    }
}
