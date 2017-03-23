package net.cnheider.movieapp.data.movie;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by heider on 06/02/17.
 */

public class MovieContract {

  public static final String CONTENT_AUTHORITY = "net.cnheider.movieapp.movieprovider";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  // Database creation SQL statement
  public static final String DATABASE_CREATE = "create table " + MovieEntry.TABLE_MOVIES + "(" +
                                               MovieEntry.COLUMN_ID + " integer primary key autoincrement, " +
                                               MovieEntry.COLUMN_TMDB_ID + " integer not null, " +
                                               MovieEntry.COLUMN_TITLE + " text not null, " +
                                               MovieEntry.COLUMN_IMAGE_URI + " text not null," +
                                               MovieEntry.COLUMN_RATING + " double not null," +
                                               MovieEntry.COLUMN_POPULARITY + " double not null," +
                                               MovieEntry.COLUMN_SYNOPSIS + " text not null," +
                                               MovieEntry.COLUMN_RELEASE_DATE + " text not null," +
                                               MovieEntry.COLUMN_SEEN + " boolean, " +
                                               MovieEntry.COLUMN_FAVORITE + " boolean, " +
                                               MovieEntry.COLUMN_PLAYLIST + " boolean " + ");";


  public static final class MovieEntry implements BaseColumns {
    public static final String TABLE_MOVIES = "movies";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_TMDB_ID = "tmdb_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE_URI = "image_uri";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_SYNOPSIS = "synopsis";
    public static final String COLUMN_SEEN = "seen";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_PLAYLIST = "playlist";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_MOVIES).build();
    public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                                                  CONTENT_AUTHORITY + "/" +
                                                  TABLE_MOVIES;    // create cursor of base type directory for multiple entries
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                                                   CONTENT_AUTHORITY + "/" +
                                                   TABLE_MOVIES;     // create cursor of base type item for single entry

    public static Uri MoviesUri(long id) {
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }
  }
}