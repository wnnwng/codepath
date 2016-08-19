package com.codepath.apps.twitter.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeTweetFragment extends DialogFragment {
    private static final int MAX_TWEET_LENGTH = 140;
    private EditText etCompose;
    private TextView tvWordCount;
    private ImageView ivProfileImage;
    private ImageView ivClose;
    private Button btnSubmit;
    private TwitterClient client;

    public ComposeTweetFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ComposeTweetFragment newInstance(String profPic) {
        ComposeTweetFragment frag = new ComposeTweetFragment();
        Bundle args = new Bundle();
        args.putString("profile_pic", profPic);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        client = TwitterApplication.getRestClient();
        etCompose = (EditText) view.findViewById(R.id.etCompose);
        tvWordCount = (TextView) view.findViewById(R.id.tvWordCount);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        ivClose = (ImageView) view.findViewById(R.id.ivClose);

        Picasso.with(getContext()).load(getArguments().getString("profile_pic")).into(ivProfileImage);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                tvWordCount.setText(Integer.toString(editable.length()));
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            client.postStatus(etCompose.getText().toString(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(getContext(), "Successfully tweeted!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(getContext(), "Couldn't post tweet. Please try again later.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            });
            }
        });

        //Picasso.with(getContext()).load(getArguments().getString("profile_pic")).into(ivProfileImage);
        getDialog().setTitle("Compose Tweet");
        // Show soft keyboard automatically and request focus to field
        etCompose.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
