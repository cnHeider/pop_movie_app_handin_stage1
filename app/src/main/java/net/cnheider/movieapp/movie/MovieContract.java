package net.cnheider.movieapp.movie;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by heider on 06/02/17.
 */

public class MovieContract {

  public static final String CONTENT_AUTHORITY = "net.cnheider.movieapp.movieprovider";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " + MovieEntry.TABLE_MOVIES + "(" + MovieEntry.COLUMN_ID + " integer primary key autoincrement, " + MovieEntry.COLUMN_TITLE + " text not null, " + MovieEntry.COLUMN_IMAGE_URI + " text not null," + MovieEntry.COLUMN_RATING + " text not null" + ");";

  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    Log.w(MovieContract.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_MOVIES);
    onCreate(database);
  }

  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  public static final class MovieEntry implements BaseColumns {
    public static final String TABLE_MOVIES = "movies";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE_URI = "image_uri";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_SYNOPSIS = "synopsis";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_MOVIES).build();
    public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;    // create cursor of base type directory for multiple entries
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;     // create cursor of base type item for single entry

    public static Uri buildFlavorsUri(long id) {
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }
  }
}