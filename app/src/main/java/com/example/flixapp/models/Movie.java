package com.example.flixapp.models;


import android.util.Log;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Headers;
@Parcel
public class Movie {

    int movieID; // int id for movie id that is used to identify a video on youtube
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    String movieReleaseDate;
    double rating; // Sets the double for rating that will be parsed


    // empty constructor needed by the Parceler library
    public Movie() {
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        rating = jsonObject.getDouble("vote_average");
        movieID = jsonObject.getInt("id");
        movieReleaseDate = jsonObject.getString("release_date");
    }


    //    Populates the movies onto a list (moviejsonArray)
    public static List<Movie> fromJsonArray(JSONArray moviejsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < moviejsonArray.length(); i++){
            movies.add(new Movie(moviejsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    //  Poster Path
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath); // Hard coding size of 342
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getMovieReleaseDate(){
        //2020-09-21 is the format we receive
        String year = movieReleaseDate.substring(0,4);
        String month = movieReleaseDate.substring(5,7);
        String day = movieReleaseDate.substring(8);
        return month +"/"+day+"/"+ year;
    }
}




//    protected Movie(Parcel in) {
//        movieImage = in.readString();
//        movieName = in.readString();
//        movieDescription = in.readString();
//        backdropImage = in.readString();
//        movieRating = in.readDouble();
//        movieReleaseDate = in.readString();
//        IMAGE_SIZES = in.createStringArrayList();
//    }

//    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
//        @Override
//        public Movie createFromParcel(Parcel in) {
//            return new Movie(in);
//        }
//        @Override
//        public Movie[] newArray(int size) {
//            return new Movie[size];
//        }
//    };


//    private void getMovieSizes(){
//        AsyncHttpClient client = new AsyncHttpClient(); //to fetch data from internet
//
//        client.get("https://api.themoviedb.org/3/configuration?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Headers headers, JSON json) {
//                Log.d("Movie", "onSuccess");
//                JSONObject jsonObject = json.jsonObject;
//
//                try {
//                    JSONArray poster_sizes = jsonObject.getJSONArray("poster_sizes");
//                    Log.i("Movie", "Poster Sizes: "+ poster_sizes);
//                    for(int i = 0; i < poster_sizes.length(); i++){
//                        IMAGE_SIZES.add(poster_sizes.getJSONObject(i).toString()); //WHERE WE SAVE THE MOVIES SIZES
//                    }
//                } catch (JSONException e) {
//                    Log.e("Movie", "Kit json exception", e);
//                    e.printStackTrace();
//                }
//
//            }
//            @Override
//            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
//                Log.d("Movie", "onFailure");
//            }
//        });
//    }
