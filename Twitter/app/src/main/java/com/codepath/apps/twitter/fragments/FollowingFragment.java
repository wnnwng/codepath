package com.codepath.apps.twitter.fragments;

import android.os.Bundle;

import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FollowingFragment extends UsersListFragment {

    public static FollowingFragment newInstance(String username) {
        FollowingFragment followingFragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        followingFragment.setArguments(args);
        return followingFragment;
    }

    protected void fetchUsers() {
        if (!loading) {
            if (cursor == 0) return;
            loading = true;
            TwitterClient client = TwitterApplication.getRestClient();

            client.getFollowing(getArguments().getString("username"), cursor, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        addAll(User.fromJSONArray(response.getJSONArray("users")));
                        cursor = response.getLong("next_cursor");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    loading = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    loading = false;
                }
            });
        }
    }
}
