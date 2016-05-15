package org.bpulse.wallpaper;

import android.net.Uri;

/**
 * Created by ben on 26/03/2016.
 */
public class WallpaperItem {

  private static final String SEPARATOR = "@@@";

  public WallpaperItem(String id, String title, String photographer, String username, String photoPage) {
    this.id = id;
    this.title = title;
    this.photographer = photographer;
    this.username = username;
    this.photoPage = photoPage;
  }

  public String id;
  public String title;
  public String photographer;
  public String username;
  public String photoPage;

  public String localFilePath;
  public Integer image;

  public Uri getPhotoPage() {
    return Uri.parse(photoPage);
  }

  public static WallpaperItem Parse(String input) {
    String[] split = input.split(SEPARATOR);

    WallpaperItem wpi =  new WallpaperItem(split[0], split[1], split[2], split[3], split[4]);
    wpi.localFilePath = split[5];
    return wpi;
  }

  public String toString() {
    return id + SEPARATOR +
            title + SEPARATOR +
            photographer + SEPARATOR +
            username + SEPARATOR +
            photoPage + SEPARATOR +
            localFilePath;
  }
}
