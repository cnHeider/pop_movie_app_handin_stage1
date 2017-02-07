package net.cnheider.movieapp.utilities;

import android.net.Uri;

import net.cnheider.movieapp.movie.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by heider on 28/01/17.
 */

public class TMDBUtilities {
  public static Movie[] getMoviesFromJson(String moviesJsonStr) throws JSONException {

    final String TMDB_MESSAGE_CODE = "error";
    final String TMDB_RESULTS = "results";
    final String TMDB_POSTER_PATH = "poster_path";
    final String TMDB_ADULT = "adult";
    final String TMDB_OVERVIEW = "overview";
    final String TMDB_RELEASE_DATE = "release_date";
    final String TMDB_GENRE_IDS = "genre_ids";
    final String TMDB_ID = "id";
    final String TMDB_ORIGINAL_TITLE = "original_title";
    final String TMDB_ORIGINAL_LANGUAGE = "original_language";
    final String TMDB_TITLE = "title";
    final String TMDB_BACKDROP_PATH = "backdrop_path";
    final String TMDB_POPULARITY = "popularity";
    final String TMDB_VOTE_COUNT = "vote_count";
    final String TMDB_VIDEO = "video";
    final String TMDB_VOTE_AVERAGE = "vote_average";

    Movie[] parsedMovieData = null;

    JSONObject moviesJSON = new JSONObject(moviesJsonStr);

    if (moviesJSON.has(TMDB_MESSAGE_CODE)) {
      int errorCode = moviesJSON.getInt(TMDB_MESSAGE_CODE);

      switch (errorCode) {
        case HttpURLConnection.HTTP_OK:
          break;
        case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
          return null;
        default:
                    /* Server probably down */
          return null;
      }
    }

    JSONArray movieArray = moviesJSON.getJSONArray(TMDB_RESULTS);

    parsedMovieData = new Movie[movieArray.length()];

    for (int i = 0; i < movieArray.length(); i++) {
      String title, synopsis, release_date;
      double rating, popularity;
      Uri poster_image;

      JSONObject movieObject = movieArray.getJSONObject(i);

      title = movieObject.getString(TMDB_TITLE);
      rating = movieObject.getDouble(TMDB_VOTE_AVERAGE);
      popularity = movieObject.getDouble(TMDB_POPULARITY);
      synopsis = movieObject.getString(TMDB_OVERVIEW);
      release_date = movieObject.getString(TMDB_RELEASE_DATE);

      String poster_image_string = movieObject.getString(TMDB_POSTER_PATH);
      poster_image = NetworkUtilities.getImageAPIUri().buildUpon().appendEncodedPath(poster_image_string).build();

      parsedMovieData[i] = new Movie(title, poster_image, rating, popularity, synopsis, release_date);
    }

    return parsedMovieData;
  }
}
