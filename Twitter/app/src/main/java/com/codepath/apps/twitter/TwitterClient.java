package com.codepath.apps.twitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.apps.twitter.models.User;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "5Lo1oYN09M3Ms8C6FePlBucEb";
	public static final String REST_CONSUMER_SECRET = "Daad4lYZGx0OrN9HYpeKvZITxbCmQeqpNJJm7bFfJ2QOiJTPqH";
	public static final String REST_CALLBACK_URL = "oauth://winnietweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public void getHomeTimeline(long oldestId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		if (oldestId > 0) {
			params.put("max_id", oldestId);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void getMentionsTimeline(long oldestId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		if (oldestId > 0) {
			params.put("max_id", oldestId);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void getUserTimeline(long oldestId, String username, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("screen_name", username);
		if (oldestId > 0) {
			params.put("max_id", oldestId);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void getUserInfo(String username, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", username);
		getClient().get(apiUrl, params, handler);
	}


    public void getProfileInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);
    }

    public void postStatus(String status, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		getClient().post(apiUrl, params, handler);
	}

    public void getFollowing(String username, long cursor, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", username);
        params.put("cursor", cursor);
        params.put("count", 50);
        getClient().get(apiUrl, params, handler);
    }

    public void getFollowers(String username, long cursor, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", username);
        params.put("cursor", cursor);
        getClient().get(apiUrl, params, handler);
    }

    public void search(String query, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("search/tweets.json");
        RequestParams params = new RequestParams();
        params.put("q", query);
        getClient().get(apiUrl, params, handler);
    }
}