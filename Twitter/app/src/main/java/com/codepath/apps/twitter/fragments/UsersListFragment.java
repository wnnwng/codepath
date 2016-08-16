package com.codepath.apps.twitter.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.twitter.EndlessScrollListener;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TweetsArrayAdapter;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.UsersArrayAdapter;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by winnie_wang on 8/18/16.
 */
public abstract class UsersListFragment extends Fragment {
    private ListView lvUsers;
    private ArrayList<User> users;
    private UsersArrayAdapter aUsers;
    protected long cursor;
    protected boolean loading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<>();
        Activity activity = getActivity();
        aUsers = new UsersArrayAdapter(activity, users);
        cursor = -1;
        loading = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users_list, parent, false);
        lvUsers = (ListView) v.findViewById(R.id.lvUsers);
        lvUsers.setAdapter(aUsers);

        lvUsers.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchUsers();
                return false;
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchUsers();
    }

    protected abstract void fetchUsers();

    protected void addAll(List<User> userList) {
        aUsers.addAll(userList);
    }
}
