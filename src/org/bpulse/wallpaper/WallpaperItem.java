package org.bpulse.wallpaper;

/**
 * Created by ben on 26/03/2016.
 */
public class WallpaperItem {

    public WallpaperItem(Integer image, String title) {
        this.image = image;
        this.title = title;
    }

    public WallpaperItem(String localFilePath, String title) {
        this.localFilePath = localFilePath;
        this.title = title;
    }

    public String localFilePath;
    public Integer image;
    public String title;
}
