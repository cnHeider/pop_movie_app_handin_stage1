package net.cnheider.movieapp.data.trailer;

/**
 * Created by heider on 21/03/17.
 */

public class Trailer {
  public String id;
  public String iso_639_1;
  public String iso_3166_1;
  public String key;
  public String name;
  public String site;
  public int size;
  public String type;


  public Trailer(String id, String iso_639_1, String iso_3166_1, String key, String name, String site, int size, String type) {
    this.id = id;
    this.iso_639_1 = iso_639_1;
    this.iso_3166_1 = iso_3166_1;
    this.key = key;
    this.name = name;
    this.site = site;
    this.size = size;
    this.type = type;
  }
}
