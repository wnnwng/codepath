package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.apps.twitter.CurrentUserSingleton;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.fragments.FollowingFragment;
import com.codepath.apps.twitter.fragments.TweetsListFragment;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class  ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    MenuItem miActionProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        final String username = getIntent().getStringExtra("username");

        if (username != null && username.length() > 0) {
            client.getUserInfo(username, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@" + user.getUsername());
                    populateProfileHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                }
            });
        } else {
            user = CurrentUserSingleton.getCurrentUser();
            getSupportActionBar().setTitle("@" + user.getUsername());
            populateProfileHeader(user);
        }

        // Get the screen name
        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(username);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit(); // changes the fragment
        }
    }

    private void populateProfileHeader(final User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagLine);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setTypeface(null, Typeface.BOLD);
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), FollowingActivity.class);
                i.putExtra("username", user.getUsername());
                startActivity(i);
            }
        });
        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), FollowersActivity.class);
                i.putExtra("username", user.getUsername());
                startActivity(i);
            }
        });
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
}
