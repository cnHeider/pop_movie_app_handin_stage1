package net.cnheider.movieapp.movie;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import net.cnheider.movieapp.utilities.DatabaseHelper;

import java.util.Arrays;
import java.util.HashSet;

public class MovieProvider extends ContentProvider {
  private static final String TAG = MovieProvider.class.getSimpleName();
  private static final int MOVIES = 100;
  private static final int MOVIE_WITH_ID = 200;
  private static final UriMatcher mUriMatcher = buildUriMatcher();
  private DatabaseHelper mDatabaseHelper;

  private static UriMatcher buildUriMatcher() {
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    final String authority = MovieContract.CONTENT_AUTHORITY;

    matcher.addURI(authority, MovieContract.MovieEntry.TABLE_MOVIES, MOVIES);
    matcher.addURI(authority, MovieContract.MovieEntry.TABLE_MOVIES + "/#", MOVIE_WITH_ID);

    return matcher;
  }

  @Override
  public boolean onCreate() {
    mDatabaseHelper = new DatabaseHelper(getContext());
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    checkColumns(projection);
    queryBuilder.setTables(MovieContract.MovieEntry.TABLE_MOVIES);

    switch (mUriMatcher.match(uri)) {
      case MOVIES:
        break;
      case MOVIE_WITH_ID:
        queryBuilder.appendWhere(MovieContract.MovieEntry.COLUMN_ID + "=" + uri.getLastPathSegment());
        break;
      default:
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    final int match = mUriMatcher.match(uri);

    switch (match) {
      case MOVIES: {
        return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
      }
      case MOVIE_WITH_ID: {
        return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
      }
      default: {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
    Uri returnUri;
    switch (mUriMatcher.match(uri)) {
      case MOVIES: {
        long _id = db.insert(MovieContract.MovieEntry.TABLE_MOVIES, null, values);
        // insert unless it is already contained in the database
        if (_id > 0) {
          returnUri = MovieContract.MovieEntry.buildFlavorsUri(_id);
        } else {
          throw new android.database.SQLException("Failed to insert row into: " + uri);
        }
        break;
      }
      default: {
        throw new UnsupportedOperationException("Unknown uri: " + uri);

      }
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return returnUri;
  }

  @Override
  public int bulkInsert(Uri uri, ContentValues[] values) {
    final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
    final int match = mUriMatcher.match(uri);
    switch (match) {
      case MOVIES:
        // allows for multiple transactions
        db.beginTransaction();

        // keep track of successful inserts
        int numInserted = 0;
        try {
          for (ContentValues value : values) {
            if (value == null) {
              throw new IllegalArgumentException("Cannot have null content values");
            }
            long _id = -1;
            try {
              _id = db.insertOrThrow(MovieContract.MovieEntry.TABLE_MOVIES, null, value);
            } catch (SQLiteConstraintException e) {
              Log.w(TAG, "Attempting to insert " +
                  value.getAsString(MovieContract.MovieEntry.COLUMN_TITLE) + " but value is already in database.");
            }
            if (_id != -1) {
              numInserted++;
            }
          }
          if (numInserted > 0) {
            db.setTransactionSuccessful();
          }
        } finally {
          db.endTransaction();
        }
        if (numInserted > 0) {
          getContext().getContentResolver().notifyChange(uri, null);
        }
        return numInserted;
      default:
        return super.bulkInsert(uri, values);
    }
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
    final int match = mUriMatcher.match(uri);
    int numDeleted;
    switch (match) {
      case MOVIES:
        numDeleted = db.delete(MovieContract.MovieEntry.TABLE_MOVIES, selection, selectionArgs);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.MovieEntry.TABLE_MOVIES + "'"); // reset _ID
        break;
      case MOVIE_WITH_ID:
        numDeleted = db.delete(MovieContract.MovieEntry.TABLE_MOVIES, MovieContract.MovieEntry._ID + " = ?", new String[]{String.valueOf(ContentUris.parseId(uri))});
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.MovieEntry.TABLE_MOVIES + "'");        // reset _ID

        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    return numDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
    int numUpdated = 0;

    if (values == null) {
      throw new IllegalArgumentException("Cannot have null content values");
    }

    switch (mUriMatcher.match(uri)) {
      case MOVIES: {
        numUpdated = db.update(MovieContract.MovieEntry.TABLE_MOVIES, values, selection, selectionArgs);
        break;
      }
      case MOVIE_WITH_ID: {
        numUpdated = db.update(MovieContract.MovieEntry.TABLE_MOVIES, values, MovieContract.MovieEntry._ID + " = ?", new String[]{String.valueOf(ContentUris.parseId(uri))});
        break;
      }
      default: {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
    }
    if (numUpdated > 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return numUpdated;
  }

  private void checkColumns(String[] projection) {
    String[] available = {MovieContract.MovieEntry.COLUMN_TITLE, MovieContract.MovieEntry.COLUMN_IMAGE_URI, MovieContract.MovieEntry.COLUMN_RATING, MovieContract.MovieEntry.COLUMN_ID};
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
  }
}