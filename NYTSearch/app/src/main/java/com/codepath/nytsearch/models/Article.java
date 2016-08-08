package com.codepath.nytsearch.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by winnie_wang on 8/8/16.
 */
@Parcel
public class Article {
    @SerializedName("web_url")
    String webUrl;
    Multimedia[] multimedia;
    Headline headline;
    
    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline.getMain();
    }

    public String getThumbnailSrc() {
        if (multimedia.length > 0) {
            return "http://www.nytimes.com/" + multimedia[0].getUrl();
        }
        return "";
    }

    public Article() { }
}
