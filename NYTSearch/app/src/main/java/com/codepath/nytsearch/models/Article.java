package com.codepath.nytsearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by winnie_wang on 8/8/16.
 */
@Parcel
public class Article {
    String webUrl;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnailSrc() {
        return thumbnailSrc;
    }

    String headline;
    String thumbnailSrc;

    public Article() {
    }

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbnailSrc = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbnailSrc = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Article> articles = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                articles.add(new Article(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return articles;
    }
}
