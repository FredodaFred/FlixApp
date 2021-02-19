package com.example.flixapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class Movie implements Parcelable {
    private String movieImage;
    private String movieName;
    private String movieDescription;
    private String backdropImage;
    private double movieRating;
    private String movieReleaseDate;
    private List<String> IMAGE_SIZES;

    public Movie(JSONObject jsonObject) throws JSONException {
        movieImage = jsonObject.getString("poster_path");
        movieName = jsonObject.getString("title");
        movieDescription = jsonObject.getString("overview");
        backdropImage = jsonObject.getString("backdrop_path");
        movieRating = jsonObject.getDouble("vote_average");
        movieReleaseDate = jsonObject.getString("release_date");
    }

    protected Movie(Parcel in) {
        movieImage = in.readString();
        movieName = in.readString();
        movieDescription = in.readString();
        backdropImage = in.readString();
        movieRating = in.readDouble();
        movieReleaseDate = in.readString();
        IMAGE_SIZES = in.createStringArrayList();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList();
        for(int i = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getBackdropImage(){
        return "https://image.tmdb.org/t/p/"+"w342"+ backdropImage;
    }
    public String getMovieImage() {
        return "https://image.tmdb.org/t/p/"+"w342"+ movieImage;
    }
    public String getMovieName() {
        return movieName;
    }
    public String getMovieDescription() {
        return movieDescription;
    }
    public double getMovieRating(){return movieRating;};
    public String getMovieReleaseDate(){return movieReleaseDate;}

    private void getMovieSizes(){
        AsyncHttpClient client = new AsyncHttpClient(); //to fetch data from internet

        client.get("https://api.themoviedb.org/3/configuration?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("Movie", "onSuccess");
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONArray poster_sizes = jsonObject.getJSONArray("poster_sizes");
                    Log.i("Movie", "Poster Sizes: "+ poster_sizes);
                    for(int i = 0; i < poster_sizes.length(); i++){
                        IMAGE_SIZES.add(poster_sizes.getJSONObject(i).toString()); //WHERE WE SAVE THE MOVIES SIZES
                    }
                } catch (JSONException e) {
                    Log.e("Movie", "Kit json exception", e);
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("Movie", "onFailure");
            }
        });



    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieImage);
        dest.writeString(movieName);
        dest.writeString(movieDescription);
        dest.writeString(backdropImage);
        dest.writeDouble(movieRating);
        dest.writeString(movieReleaseDate);
        dest.writeStringList(IMAGE_SIZES);
    }
}
