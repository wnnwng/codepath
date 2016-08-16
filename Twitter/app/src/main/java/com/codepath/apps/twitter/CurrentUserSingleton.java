package com.codepath.apps.twitter;

import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CurrentUserSingleton {
    private CurrentUserSingleton() { }

    public static User getCurrentUser()
    {
        if (user == null) setUser();
        return user;
    }

    public static void setUser() {
        TwitterClient client = TwitterApplication.getRestClient();
        client.getProfileInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
            }
        });
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    private static User user;
}