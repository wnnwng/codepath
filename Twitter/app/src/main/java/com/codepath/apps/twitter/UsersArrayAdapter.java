package com.codepath.apps.twitter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.models.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class UsersArrayAdapter extends ArrayAdapter<User> {
    public UsersArrayAdapter(Context context, List<User> users) {
        super(context, android.R.layout.simple_list_item_1);
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvUserName;
        TextView tvTagLine;
        ImageView ivProfileImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            vh = new ViewHolder();
            vh.tvName = (TextView) convertView.findViewById(R.id.tvName);
            vh.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            vh.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            vh.tvTagLine = (TextView) convertView.findViewById(R.id.tvTagLine);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tvName.setText(user.getName());
        vh.tvName.setTypeface(null, Typeface.BOLD);
        vh.tvUserName.setText("@" + user.getUsername());
        vh.tvTagLine.setText(user.getTagline());
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(vh.ivProfileImage);
        return convertView;
    }
}
