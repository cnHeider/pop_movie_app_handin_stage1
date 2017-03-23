package net.cnheider.movieapp.utilities;

import android.net.Uri;

import net.cnheider.movieapp.data.movie.Movie;
import net.cnheider.movieapp.data.review.Review;
import net.cnheider.movieapp.data.trailer.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by heider on 28/01/17.
 */

public class TMDBUtilities {
  final static String TMDB_MESSAGE_CODE = "error";
  final static String TMDB_RESULTS = "results";

  public static Movie[] getMoviesFromJson(String moviesJsonStr) throws JSONException {

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
      int id;
      Uri poster_image;

      JSONObject movieObject = movieArray.getJSONObject(i);

      id = movieObject.getInt(TMDB_ID);
      title = movieObject.getString(TMDB_TITLE);
      rating = movieObject.getDouble(TMDB_VOTE_AVERAGE);
      popularity = movieObject.getDouble(TMDB_POPULARITY);
      synopsis = movieObject.getString(TMDB_OVERVIEW);
      release_date = movieObject.getString(TMDB_RELEASE_DATE);

      String poster_image_string = movieObject.getString(TMDB_POSTER_PATH);
      poster_image = NetworkUtilities.getImageAPIUri().buildUpon().appendEncodedPath(poster_image_string).build();

      parsedMovieData[i] = new Movie(id, title, poster_image, rating, popularity, synopsis, release_date);
    }

    return parsedMovieData;
  }

  public static Trailer[] getTrailersForMovieJson(String trailerJsonStr) throws JSONException {

    final String TMDB_TRAILER_ID = "id";
    final String TMDB_TRAILER_ISO_639_1 = "iso_639_1";
    final String TMDB_TRAILER_ISO_3611_1 = "iso_3166_1";
    final String TMDB_TRAILER_KEY = "key";
    final String TMDB_TRAILER_NAME = "name";
    final String TMDB_TRAILER_SITE = "site";
    final String TMDB_TRAILER_SIZE = "size";
    final String TMDB_TRAILER_TYPE = "type";

    Trailer[] parsedTrailerData = null;

    JSONObject trailerJSON = new JSONObject(trailerJsonStr);

    if (trailerJSON.has(TMDB_MESSAGE_CODE)) {
      int errorCode = trailerJSON.getInt(TMDB_MESSAGE_CODE);

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

    JSONArray trailerArray = trailerJSON.getJSONArray(TMDB_RESULTS);

    parsedTrailerData = new Trailer[trailerArray.length()];

    for (int i = 0; i < trailerArray.length(); i++) {
      String id, iso_639_1, iso_3166_1, key, name, site, type;
      int size;

      JSONObject trailerObject = trailerArray.getJSONObject(i);

      id = trailerObject.getString(TMDB_TRAILER_ID);
      iso_639_1 = trailerObject.getString(TMDB_TRAILER_ISO_639_1);
      iso_3166_1 = trailerObject.getString(TMDB_TRAILER_ISO_3611_1);
      key = trailerObject.getString(TMDB_TRAILER_KEY);
      name = trailerObject.getString(TMDB_TRAILER_NAME);
      site = trailerObject.getString(TMDB_TRAILER_SITE);
      size = trailerObject.getInt(TMDB_TRAILER_SIZE);
      type = trailerObject.getString(TMDB_TRAILER_TYPE);

      parsedTrailerData[i] = new Trailer(id, iso_639_1, iso_3166_1, key, name, site, size, type);
    }

    return parsedTrailerData;
  }

  public static Review[] getReviewsForMovieJson(String reviewJsonStr) throws JSONException {

    final String TMDB_REVIEW_ID = "id";
    final String TMDB_REVIEW_AUTHOR = "author";
    final String TMDB_REVIEW_CONTENT = "content";
    final String TMDB_REVIEW_URL = "url";

    Review[] parsedReviewData = null;

    JSONObject reviewsJSON = new JSONObject(reviewJsonStr);

    if (reviewsJSON.has(TMDB_MESSAGE_CODE)) {
      int errorCode = reviewsJSON.getInt(TMDB_MESSAGE_CODE);

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

    JSONArray reviewArray = reviewsJSON.getJSONArray(TMDB_RESULTS);

    parsedReviewData = new Review[reviewArray.length()];

    for (int i = 0; i < reviewArray.length(); i++) {
      String id, author, content, url;

      JSONObject trailerObject = reviewArray.getJSONObject(i);

      id = trailerObject.getString(TMDB_REVIEW_ID);
      author = trailerObject.getString(TMDB_REVIEW_AUTHOR);
      content = trailerObject.getString(TMDB_REVIEW_CONTENT);
      url = trailerObject.getString(TMDB_REVIEW_URL);

      parsedReviewData[i] = new Review(id, author, content, url);
    }

    return parsedReviewData;
  }
}
