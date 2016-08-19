package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.fragments.FollowingFragment;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.fragments.UsersListFragment;

/**
 * Created by winnie_wang on 8/18/16.
 */
public class FollowingActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        final String username = getIntent().getStringExtra("username");


        // Get the screen name
        if (savedInstanceState == null) {
            FollowingFragment followingFragment = FollowingFragment.newInstance(username);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, followingFragment);
            ft.commit(); // changes the fragment
        }
    }
}
