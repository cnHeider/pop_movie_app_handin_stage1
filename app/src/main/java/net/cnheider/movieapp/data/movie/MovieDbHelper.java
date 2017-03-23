package net.cnheider.movieapp.data.movie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by heider on 20/03/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "movie.db";
  private static final String TAG = MovieProvider.class.getSimpleName();
  private static final int DATABASE_VERSION = 4;


  public MovieDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.d(TAG, "onCreate: ");
    db.execSQL(MovieContract.DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion +
               ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_MOVIES);
    onCreate(db);
  }
}
