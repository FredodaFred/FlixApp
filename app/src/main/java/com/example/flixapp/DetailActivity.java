package com.example.flixapp;

import android.content.Intent;
import android.media.Rating;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixapp.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;
//New imports
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class DetailActivity extends AppCompatActivity  {
    public Movie dMovie;
    public TextView detailTitle;
    public TextView detailDescription;
    public TextView dateReleased;
    public RatingBar movieRating;
    public YouTubePlayerView ytPrv;
    public static final String YT_API_KEY = "AIzaSyAvCo-5EM-Yw3fa6vZDzicWiwgBF6K-9Vg";
    public static final String YT_GET_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=6cbc317ba7768c9036a8551da2bddc1d";
    public static final String TAG = "DetailActivity";
    public Button backButton;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);

        detailTitle = findViewById(R.id.movie_name);
        detailDescription = findViewById(R.id.movie_description);
        dateReleased = findViewById(R.id.detail_release_date);
        ytPrv = findViewById(R.id.video_item);
        movieRating = findViewById(R.id.ratings_bar);
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });

        Log.d("DetailActivity", "OnCreate Called");

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        detailTitle.setText(movie.getTitle());
        detailDescription.setText(movie.getOverview());
        dateReleased.setText(movie.getMovieReleaseDate());
        float rating = (float)movie.getRating()/2;
        Log.d("Details", String.valueOf(rating));
        movieRating.setRating(rating);
        // moved async
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(YT_GET_URL, movie.getMovieID()), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0){
                        return;
                    }
                    String YoutubeKey = results.getJSONObject(0).getString("key");
                    Log.d("DetailActivity", YoutubeKey);
                    initializeYoutube(YoutubeKey);
                } catch (JSONException e) {

                    Log.e("DetailActivity", "Failed to parse JSON", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"Youtube onFailure");
            }
        });

    }
    private void initializeYoutube(final String ytKey) {

        YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.video_item);
        youtubeFragment.initialize(ytKey,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(ytKey);
                        Log.d("DetailActivity", "onInitializationSuccess");
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Log.d("DetailActivity", "onInitializationFailure");
                    }
                });
    }
//    private void initializeYoutube(final String ytKey){
//        ytPrv.initialize(ytKey, new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                Log.d("Detail Activty", "YT Initialize Success");
//                youTubePlayer.cueVideo(ytKey);
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                Log.d("Detail Activty", "YT Initialize Failure");
//            }
//        });
//    }
}
