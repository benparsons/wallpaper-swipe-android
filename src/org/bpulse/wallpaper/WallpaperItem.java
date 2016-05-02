package org.bpulse.wallpaper;

import android.net.Uri;

/**
 * Created by ben on 26/03/2016.
 */
public class WallpaperItem {

    public WallpaperItem(String title, String photographer, String username, Uri photoPage) {
      this.title = title;
      this.photographer = photographer;
      this.username = username;
      this.photoPage = photoPage;
    }

    public WallpaperItem(String title, Integer image) {
        this.image = image;
        this.title = title;
    }

    public WallpaperItem(String title, String localFilePath, String username) {
        this.localFilePath = localFilePath;
        this.title = title;
    }

    public String localFilePath;
    public Integer image;
    public String title;
    public String photographer;
    public String username;
    public Uri photoPage;
}
