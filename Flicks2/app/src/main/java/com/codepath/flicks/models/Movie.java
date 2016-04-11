package com.codepath.flicks.models;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.codepath.flicks.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by winnie_wang on 8/1/16.
 */
public class Movie implements Serializable {
    private final static double POPULAR_RATING_THRESHOLD = 5.0;
    private String overview;
    private String title;
    private String posterImgPath;
    private String backdropImgPath;
    private String releaseDate;
    private Double rating;
    private String id;
    private DisplayType display;

    public enum DisplayType {
        WITH_DETAILS, BACKDROP_ONLY
    }

    public String getOverview() {
        return overview;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getReleaseDate() { return releaseDate; }

    public Double getRating() { return rating; }

    public String getPosterImgPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterImgPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", backdropImgPath);
    }

    public DisplayType getDisplay() { return display; }

    public Movie(JSONObject json) throws JSONException {
        this.overview = json.getString("overview");
        this.title = json.getString("original_title");
        this.posterImgPath = json.getString("poster_path");
        this.backdropImgPath = json.getString("backdrop_path");
        this.rating = json.getDouble("vote_average");
        this.display = isPopular() ? DisplayType.BACKDROP_ONLY : DisplayType.WITH_DETAILS;
        this.releaseDate = json.getString("release_date");
        this.id = json.getString("id");
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext())
            .load(url)
            .transform(new RoundedCornersTransformation(5, 5))
            .placeholder(R.drawable.loading_icon)
            .into(view);
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> movieList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                movieList.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return movieList;
    }

    public boolean isPopular() {
        return rating > POPULAR_RATING_THRESHOLD;
    }
}
