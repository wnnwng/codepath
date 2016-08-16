package com.codepath.apps.twitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Tweets")
public class Tweet extends Model {
    @Column(name = "body")
    private String body;
    @Column(name= "uid", unique = true)
    private long uid;
    @Column(name = "user")
    private User user;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "category")
    private String category;

    public static Tweet fromJSON(JSONObject json, String category) {
        Tweet tweet = new Tweet();
        try {
            long uid = json.getLong("id");

            List<Tweet> existingTweets = (new Select()).from(Tweet.class).where("uid = ?", uid).execute();
            if (existingTweets.size() > 0) {
                return existingTweets.get(0);
            }

            tweet.body = json.getString("text");
            tweet.uid = uid;
            tweet.createdAt= json.getString("created_at");
            tweet.user = User.fromJSON(json.getJSONObject("user"));
            tweet.category = category;
            tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray response, String category) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                tweets.add(Tweet.fromJSON(response.getJSONObject(i), category));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }
}
