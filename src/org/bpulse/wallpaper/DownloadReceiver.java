package org.bpulse.wallpaper;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.database.Cursor;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by benp on 26/04/2016.
 * based on http://blog.evizija.si/android-download-manager/
 */
public class DownloadReceiver extends BroadcastReceiver {
  public DownloadReceiver() {}

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();

    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
      long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
      DownloadManager.Query query = new DownloadManager.Query();
      query.setFilterById(downloadId);
      DownloadManager mDownloadManager =
              (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
      Cursor c = mDownloadManager.query(query);
      if (c.moveToFirst()) {
        int columnIndex = c
                .getColumnIndex(DownloadManager.COLUMN_STATUS);

        //if completed successfully
        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)){
          String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
          //check if this is your download uri
          //if (!uri.contains("com.example.app"))
          //  return;

          //here you get file path so you can move
          //it to other location if you want
          String downloadedPackageUriString =
                  c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

          //notify your app that download was completed
          //with local broadcast receiver
          Intent localReceiver = new Intent("file-ready");
          localReceiver.putExtra("localUrl", downloadedPackageUriString);
          localReceiver.putExtra("downloadId", downloadId);
          LocalBroadcastManager
                  .getInstance(context)
                  .sendBroadcast(localReceiver);
        }else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)){
          //if failed you can make a retry or whatever
          //I just delete my id from sqllite
        }
      }
    }
  }
}
