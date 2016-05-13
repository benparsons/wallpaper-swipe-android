package org.bpulse.wallpaper;

import android.net.Uri;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by ben on 26/03/2016.
 */
public class WallpaperItem {

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

  public Set<String> toStringSet() {
    if (localFilePath == null || localFilePath.length() == 0) {
      String error = "null or empty localFilePath while attempting to save";
      Log.e("wallpaper-item", error);
      throw new IllegalStateException(error);
    }

    LinkedHashSet<String> result = new LinkedHashSet<String>();
    result.add(id);
    result.add(title);
    result.add(photographer);
    result.add(username);
    result.add(localFilePath);
    return result;
  }
}
