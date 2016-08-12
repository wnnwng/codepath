package com.codepath.nytsearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by winnie_wang on 8/12/16.
 */
public class Response {
    @SerializedName("docs")
    ArrayList<Article> docs;

    public Response() {
        docs = new ArrayList<>();
    }

    public ArrayList<Article> getArticles() {
        return docs;
    }
}
