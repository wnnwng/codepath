package com.codepath.flicks.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.codepath.flicks.R;
import com.codepath.flicks.constants.MovieEndpoints;
import com.codepath.flicks.databinding.ActivityMovieDetailBinding;
import com.codepath.flicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by winnie_wang on 8/2/16.
 */
public class MovieDetailActivity extends YouTubeBaseActivity {
    Movie movie;
    private ActivityMovieDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        movie = (Movie) getIntent().getSerializableExtra("movie");
        binding.setMovie(movie);
        setUpYoutube();
    }

    private void setUpYoutube() {
        String id = movie.getId();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize("AIzaSyBiK3RiA47cwkIhzlNq85B1dJfAEgZGS24",
            new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                    YouTubePlayer youTubePlayer, boolean b) {

                    client.get(String.format(MovieEndpoints.TRAILERS, id), params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                JSONArray trailerResults = response.getJSONArray("youtube");
                                final String trailerId = trailerResults.getJSONObject(0).getString("source");
                                youTubePlayer.cueVideo(trailerId);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                    YouTubeInitializationResult youTubeInitializationResult) {

                }
            }
        );
    }
}
