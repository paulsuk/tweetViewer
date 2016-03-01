package com.example.minkangpaulsuk.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedorvlasov.lazylist.ImageLoader;


/*
 * This is the code to display the more details when a list element is pressed.
 */
public class MoreDetails extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        ImageLoader imageLoader = tweetAdapter.getImageLoader();

        Intent i = getIntent();

        String username = i.getStringExtra("username");
        String postedDate = i.getStringExtra("postedDate");
        String content = i.getStringExtra("content");
        String imageURL = i.getStringExtra("imageURL");
        String favoriteCount = i.getStringExtra("favoriteCount");
        String retweetCount = i.getStringExtra("retweetCount");

        TextView usernameView = (TextView) findViewById(R.id.moreUsername);
        TextView postedDateView = (TextView) findViewById(R.id.morePostedDate);
        TextView contentView = (TextView) findViewById(R.id.moreContent);
        ImageView imageView = (ImageView) findViewById(R.id.moreImage);
        TextView favoriteView = (TextView) findViewById(R.id.moreFavorites);
        TextView retweetView = (TextView) findViewById(R.id.moreRetweets);

        usernameView.setText(username);
        postedDateView.setText(postedDate);
        contentView.setText(content);
        imageLoader.DisplayImage(imageURL, imageView);
        favoriteView.setText(favoriteCount);
        retweetView.setText(retweetCount);

    }

}
