package org.bpulse.wallpaper;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.database.Cursor;
import android.database.Cursor;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.*;
import java.util.Arrays;

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

          String[] uriParts = downloadedPackageUriString.split("/");

          String downloadedPackageUriString2 = moveFile(TextUtils.join("/", Arrays.copyOfRange(uriParts, 0, uriParts.length - 1)) + "/",
                  uriParts[uriParts.length - 1],
                  Environment.getExternalStorageDirectory().getAbsolutePath() + "/wallpaper/");

          //notify your app that download was completed
          //with local broadcast receiver
          Intent localReceiver = new Intent("file-ready");
          localReceiver.putExtra("localUrl", downloadedPackageUriString2);
          localReceiver.putExtra("downloadId", downloadId);
          LocalBroadcastManager
                  .getInstance(context)
                  .sendBroadcast(localReceiver);
        }else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)){
          //if failed you can make a retry or whatever
          //I just delete my id from sqllite
        }
      }
      c.close();
    }
  }

  // taken from http://stackoverflow.com/a/11327789/384316
  private String moveFile(String inputPath, String inputFile, String outputPath) {
    InputStream in = null;
    OutputStream out = null;
    try {

      //create output directory if it doesn't exist
      File dir = new File (outputPath);
      if (!dir.exists())
      {
        dir.mkdirs();
      }


      in = new FileInputStream(inputPath + inputFile);
      out = new FileOutputStream(outputPath + inputFile);

      byte[] buffer = new byte[1024];
      int read;
      while ((read = in.read(buffer)) != -1) {
        out.write(buffer, 0, read);
      }
      in.close();
      in = null;

      // write the output file
      out.flush();
      out.close();
      out = null;

      return outputPath + inputFile;
    }

    catch (FileNotFoundException fnfe1) {
      Log.e("tag", fnfe1.getMessage());
    }
    catch (Exception e) {
      Log.e("tag", e.getMessage());
    }

    return "";

  }
}
