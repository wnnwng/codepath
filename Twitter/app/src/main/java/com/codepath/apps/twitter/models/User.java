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

@Table(name = "Users")
public class User extends Model {
    @Column(name = "name")
    private String name;
    @Column(name = "uid", unique = true)
    private long uid;
    @Column(name = "screen_name", unique = true)
    private String username;
    @Column(name = "profile_img_url")
    private String profileImageUrl;
    @Column(name = "tagline")
    private String tagline;
    @Column(name = "followers_count")
    private int followersCount;
    @Column(name = "following_count")
    private int followingCount;

    public static User fromJSON(JSONObject json) {
        User u = new User();
        try {
            String username = json.getString("screen_name");
            List<User> existingUsers = (new Select()).from(User.class).where("screen_name = ?", username).execute();
            if (existingUsers.size() > 0) {
                return existingUsers.get(0);
            }
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.username = username;
            u.profileImageUrl = json.getString("profile_image_url");
            u.tagline = json.getString("description");
            u.followersCount = json.getInt("followers_count");
            u.followingCount = json.getInt("friends_count");
            u.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public static List<User> fromJSONArray(JSONArray response) {
        ArrayList<User> userList = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                userList.add(User.fromJSON(response.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }

    public static ArrayList<Long> getUserIds(JSONArray response) {
        ArrayList<Long> idList = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                idList.add(response.getLong(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return idList;
    }
}
