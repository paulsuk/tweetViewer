package com.example.minkangpaulsuk.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "a7oe6yzwdlZn7i4Znl1qoLyPP";
    private static final String TWITTER_SECRET = "3CFygUxokCDUpL2YTAgGmbHPMmbVF81GtZHT0JKwg66zpohofr";

    //initializing some things because they are called in many different methods,instances
    ListView listView;
    List<Map<String, String>> tweetData = new ArrayList<>();
    tweetAdapter myTweetAdapter;

    //*****Set this value here to use change number of inputs
    private int maxEntries = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        tweetData = new ArrayList<>();
        listView = (ListView) findViewById(R.id.Feed);




        final List<Map<String, String>> moreTweetInfo = new ArrayList<>();

        myTweetAdapter = new tweetAdapter(this, tweetData, R.layout.content_tweet, new String[]{"content", "username", "thumbnail"}, new int[]{R.id.tweetcontent, R.id.tweetusername, R.id.thumbnail});


        Button button = (Button) findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the old data first
                tweetData.clear();
                moreTweetInfo.clear();

                AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.searchBar);
                String keyword = tv.getText().toString();

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

                SearchService searchService = twitterApiClient.getSearchService();
                searchService.tweets(keyword, null, "en", null, "popular", 100, null, null, null, null, new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {

                        if (result.data.tweets.size() > 0) {
                            for (int i = 0; i < min(result.data.tweets.size(), maxEntries); i++) {

                                Map<String, String> tweet = new HashMap<>();
                                Map<String, String> moreTweet = new HashMap<>();
                                String mediaURL = "";
                                String thumbURL = "";

                                Tweet foundTweet = result.data.tweets.get(i);

                                //Getting the pictures
                                if (foundTweet.entities.media != null) {
                                    mediaURL = result.data.tweets.get(i).entities.media.get(0).mediaUrl + ":large";
                                    thumbURL = result.data.tweets.get(i).entities.media.get(0).mediaUrl + ":thumb";
                                    Log.e("mediaURL", String.valueOf(i) + mediaURL);
                                }

                                //Getting the basic information to display the tweets
                                if (foundTweet.truncated) {
                                    tweet.put("content", foundTweet.retweetedStatus.text);
                                    moreTweet.put("content", foundTweet.retweetedStatus.text);
                                } else {
                                    tweet.put("content", foundTweet.text);
                                    moreTweet.put("content", foundTweet.text);
                                }
                                tweet.put("username", "Submitted by: " + "@" + foundTweet.user.name);
                                tweet.put("thumbnail", thumbURL);
                                tweetData.add(tweet);

                                //This is additional information that will be displayed in content_more_details
                                moreTweet.put("username", "@" + foundTweet.user.name);
                                moreTweet.put("postedDate", foundTweet.createdAt);
                                if (foundTweet.favorited) {
                                    moreTweet.put("favoriteCount", "Favorites: " + String.valueOf(foundTweet.favoriteCount));
                                } else {
                                    moreTweet.put("favoriteCount", "Favorites: 0");
                                }
                                if (foundTweet.retweeted) {
                                    moreTweet.put("retweetCount", "Retweets: " + String.valueOf(foundTweet.retweetCount));
                                } else {
                                    moreTweet.put("retweetCount", "Retweets: 0");
                                }
                                moreTweet.put("imageURL", mediaURL);
                                moreTweetInfo.add(moreTweet);
                            }
                            listView.setAdapter(myTweetAdapter);
                        } else {
                            //Incase the search returns no tweets
                            Map<String, String> tweet = new HashMap<>();
                            tweet.put("username", "There seems to be nothing here...");
                            tweet.put("content", "");
                            tweet.put("thumbnail", "");
                            ;
                        }
                    }

                    ;

                    // When there is some issue with the search
                    @Override
                    public void failure(TwitterException e) {
                        Map<String, String> tweet = new HashMap<>();
                        tweet.put("username", "There was some error...");
                        tweet.put("content", "");
                        e.printStackTrace();
                    }
                });

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        // Seeing more details
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //Creating intent to go to new view
                Intent i = new Intent(getApplicationContext(), MoreDetails.class);
                Map<String, String> tempList = moreTweetInfo.get(position);

                //All the extra details to go to the next view
                i.putExtra("username", tempList.get("username"));
                i.putExtra("content", tempList.get("content"));
                i.putExtra("postedDate", tempList.get("postedDate"));
                i.putExtra("favoriteCount", tempList.get("favoriteCount"));
                i.putExtra("retweetCount", tempList.get("retweetCount"));
                i.putExtra("imageURL", tempList.get("imageURL"));

                startActivity(i);
            }

        });

    }
}

