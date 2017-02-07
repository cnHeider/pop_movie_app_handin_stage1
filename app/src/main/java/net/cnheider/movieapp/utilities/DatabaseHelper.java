package net.cnheider.movieapp.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.cnheider.movieapp.movie.MovieContract;

/**
 * Created by heider on 06/02/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "movie_app.db";
  private static final int DATABASE_VERSION = 1;

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    MovieContract.onCreate(database);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    MovieContract.onUpgrade(database, oldVersion, newVersion);
  }

}
