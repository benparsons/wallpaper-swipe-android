package org.bpulse.wallpaper;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import java.util.List;

/**
 * Created by ben on 24/04/2016.
 */
public class ImageDownloader {
  DownloadManager downloadManager;
  long myDownloadReference;

  public ImageDownloader(Context context){
    downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
  }

  public void DownloadImage(String url) {
    DownloadImage(Uri.parse(url));
  }

  public void DownloadImage(Uri uri) {
    DownloadManager.Request request = new DownloadManager.Request(uri);
    request.setTitle("download title").setDescription("download description");
    myDownloadReference = downloadManager.enqueue(request);
  }
}
