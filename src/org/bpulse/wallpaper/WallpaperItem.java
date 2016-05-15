package org.bpulse.wallpaper;

import android.net.Uri;

/**
 * Created by ben on 26/03/2016.
 */
public class WallpaperItem {

  private static final String SEPARATOR = "@@@";

  public WallpaperItem(String id, String title, String photographer, String username) {
    this.id = id;
    this.title = title;
    this.photographer = photographer;
    this.username = username;
  }

  public String id;
  public String title;
  public String photographer;
  public String username;

  public String localFilePath;
  public Integer image;

  public Uri getPhotoPage() {
    return Uri.parse("https://www.flickr.com/photos/" + username + "/" + id + "/");
  }

  public static WallpaperItem Parse(String input) {
    String[] split = input.split(SEPARATOR);

    WallpaperItem wpi =  new WallpaperItem(split[0], split[1], split[2], split[3]);
    wpi.localFilePath = split[4];
    return wpi;
  }

  public String toString() {
    return id + SEPARATOR +
            title + SEPARATOR +
            photographer + SEPARATOR +
            photographer + SEPARATOR +
            localFilePath;
  }
}
