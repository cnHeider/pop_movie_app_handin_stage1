package net.cnheider.movieapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by heider on 27/01/17.
 */

public class NetworkUtilities {
  final static String QUERY_PARAM = "q";
  final static String T_PARAM = "t";
  final static String POSTER_PARAM = "p";
  final static String SIZE_PARAM = "";
  final static String FORMAT_PARAM = "mode";
  final static String UNITS_PARAM = "units";
  final static String DAYS_PARAM = "cnt";
  private static final String TAG = NetworkUtilities.class.getSimpleName();
  private static final String IMAGE_BASEURL = "http://image.tmdb.org/t/p/";
  private static final String API_BASEURL = "https://api.themoviedb.org/3/movie";
  private static final String POPULAR_PATH = "popular";
  private static final String TOP_RATED_PATH = "top_rated";
  private static final String API_KEY_PARAM = "api_key";
  private static final String format = "json";
  private static final String size_w92 = "w92";
  private static final String size_w154 = "w154";
  private static final String size_w185 = "w185";
  private static final String size_w342 = "w342";
  private static final String size_ww500 = "w500";
  private static final String size_w780 = "w780";
  private static final String size_original = "original";

  private static final String size_default = size_w185;

  private static final String auth_v3_api_key = "insert key here"; //TODO: insert api key from tmdb

  public static URL getPopularURL() {
    Uri builtUri = Uri.parse(API_BASEURL).buildUpon().appendPath(POPULAR_PATH).appendQueryParameter(API_KEY_PARAM, auth_v3_api_key).build();

    URL url = null;
    try {
      url = new URL(builtUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    Log.v(TAG, "Built URI " + url);

    return url;
  }

  public static URL getTopRatedURL() {
    Uri builtUri = Uri.parse(API_BASEURL).buildUpon().appendPath(TOP_RATED_PATH).appendQueryParameter(API_KEY_PARAM, auth_v3_api_key).build();

    URL url = null;
    try {
      url = new URL(builtUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    Log.v(TAG, "Built URI " + url);

    return url;
  }

  /**
   * This method returns the entire result from the HTTP response.
   *
   * @param url The URL to fetch the HTTP response from.
   * @return The contents of the HTTP response.
   * @throws IOException Related to network and stream reading
   */
  public static String getResponseFromHttpUrl(URL url) throws IOException {
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    try {
      InputStream in = urlConnection.getInputStream();

      Scanner scanner = new Scanner(in);
      scanner.useDelimiter("\\A");

      boolean hasInput = scanner.hasNext();
      if (hasInput) {
        return scanner.next();
      } else {
        return null;
      }
    } finally {
      urlConnection.disconnect();
    }
  }

  public static Uri getAPIUri() {
    Uri builtUri = Uri.parse(API_BASEURL);

    return builtUri;
  }

  public static Uri getImageAPIUri() {
    Uri builtUri = Uri.parse(IMAGE_BASEURL).buildUpon().appendPath(size_w185).build();

    return builtUri;
  }

  public static boolean isOnline(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }
}
