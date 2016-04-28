package org.bpulse.wallpaper;

/**
 * Created by ben on 26/03/2016.
 */
public class WallpaperItem {

    public WallpaperItem(String title) {
        this.title = title;
    }

    public WallpaperItem(String title, Integer image) {
        this.image = image;
        this.title = title;
    }

    public WallpaperItem(String title, String localFilePath) {
        this.localFilePath = localFilePath;
        this.title = title;
    }

    public String localFilePath;
    public Integer image;
    public String title;
}
