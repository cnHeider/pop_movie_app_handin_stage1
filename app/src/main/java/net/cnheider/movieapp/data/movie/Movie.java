package net.cnheider.movieapp.data.movie;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heider on 27/01/17.
 */

public class Movie implements Parcelable {
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

  public int id;
  public String title;
  public String synopsis;
  public Uri poster_image;
  public double user_rating;
  public double popularity;
  public String release_date;
  String[] Genres;

  public Movie() {
  }

  public Movie(int id, String title, Uri poster_image, double user_rating, double popularity, String synopsis, String release_date) {
    this(id, title, poster_image, user_rating, popularity);
    this.synopsis = synopsis;
    this.release_date = release_date;
  }

  public Movie(int id, String title, Uri poster_image, double user_rating, double popularity) {
    this.id = id;
    this.title = title;
    this.poster_image = poster_image;
    this.user_rating = user_rating;
    this.popularity = popularity;
  }

  protected Movie(Parcel in) {
    id = in.readInt();
    title = in.readString();
    synopsis = in.readString();
    poster_image = in.readParcelable(Uri.class.getClassLoader());
    user_rating = in.readDouble();
    popularity = in.readDouble();
    Genres = in.createStringArray();
    release_date = in.readString();
  }

  public Date getReleaseDate() {
    Date date_object = new Date();
    try {
      new SimpleDateFormat("yyyy-MM-dd").parse(release_date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date_object;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(title);
    dest.writeString(synopsis);
    dest.writeParcelable(poster_image, flags);
    dest.writeDouble(user_rating);
    dest.writeDouble(popularity);
    dest.writeStringArray(Genres);
    dest.writeString(release_date);
  }

  @Override
  public String toString() {
    return title + ": " + String.valueOf(user_rating);
  }
}
