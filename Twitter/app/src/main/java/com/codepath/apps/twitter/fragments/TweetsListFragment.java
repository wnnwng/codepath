package com.codepath.apps.twitter.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.codepath.apps.twitter.EndlessScrollListener;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TweetsArrayAdapter;
import com.codepath.apps.twitter.models.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment {
    private ListView lvTweets;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private SwipeRefreshLayout swipeContainer;
    protected boolean loading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        Activity activity = getActivity();
        aTweets = new TweetsArrayAdapter(activity, tweets);
        loading = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        setUpRefreshColors();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            fetchTweets(-1, true);
            }
        });

        // To prevent exceeding api limit uncomment later
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                long oldestId = -1;
                if (tweets.size() > 0) {
                    oldestId = tweets.get(tweets.size() - 1).getUid();
                }
                fetchTweets(oldestId, false);
                return false;
            }
        });
        return v;
    }


    public void populateTimeline(long oldestId, boolean clearData, String category) {
        if (isOnline()) {
            SQLiteUtils.execSql("DELETE FROM Tweets");
            SQLiteUtils.execSql("DELETE FROM Users");
            fetchTweets(oldestId, clearData);
        } else {
            String c = category;
            List<Tweet> tweets = (new Select()).from(Tweet.class).where("Category = ?", c).execute();
            addAll(tweets, true);
        }
    }

    protected abstract void fetchTweets(long oldestId, boolean clearData);

    public void addAll(List<Tweet> newTweets, boolean clearData) {
        if (clearData) {
            aTweets.clear();
        }
        aTweets.addAll(newTweets);
    }

    protected void setSwipeRefreshing(boolean refreshing) {
        swipeContainer.setRefreshing(refreshing);
    }

    private void setUpRefreshColors() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
