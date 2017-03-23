package net.cnheider.movieapp.utilities;

import net.cnheider.movieapp.data.movie.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by heider on 03/02/17.
 */

public class SortingUtilities {

  public static ArrayList<Movie> sortByReleaseDate(ArrayList<Movie> in, final boolean ascending) {
    Collections.sort(in, new Comparator<Movie>() {
      public int compare(Movie m1, Movie m2) {
        if (ascending)
          return m1.release_date.compareTo(m2.release_date);
        return m2.release_date.compareTo(m1.release_date);
      }
    });
    return in;
  }

  public static ArrayList<Movie> sortByTitle(ArrayList<Movie> in, final boolean ascending) {
    Collections.sort(in, new Comparator<Movie>() {
      public int compare(Movie m1, Movie m2) {
        if (ascending)
          return m1.title.compareTo(m2.title);
        return m2.title.compareTo(m1.title);
      }
    });
    return in;
  }

  public static ArrayList<Movie> sortByRating(ArrayList<Movie> in, final boolean ascending) {
    Collections.sort(in, new Comparator<Movie>() {
      public int compare(Movie m1, Movie m2) {
        if (ascending)
          return Double.compare(m1.user_rating, m2.user_rating);
        return Double.compare(m2.user_rating, m1.user_rating);
      }
    });
    return in;
  }

  public static ArrayList<Movie> sortByPopularity(ArrayList<Movie> in, final boolean ascending) {
    Collections.sort(in, new Comparator<Movie>() {
      public int compare(Movie m1, Movie m2) {
        if (ascending)
          return Double.compare(m1.popularity, m2.popularity);
        return Double.compare(m2.popularity, m1.popularity);
      }
    });
    return in;
  }
}
