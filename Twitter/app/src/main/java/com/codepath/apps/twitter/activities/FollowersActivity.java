package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.fragments.FollowersFragment;
import com.codepath.apps.twitter.fragments.FollowingFragment;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.fragments.UsersListFragment;

public class FollowersActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        final String username = getIntent().getStringExtra("username");

        // Get the screen name
        if (savedInstanceState == null) {
            FollowersFragment followersFragment = FollowersFragment.newInstance(username);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, followersFragment);
            ft.commit(); // changes the fragment
        }
    }
}
