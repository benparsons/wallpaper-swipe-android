package org.bpulse.wallpaper;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
  ImagePagerAdapter adapter;
  ViewPager viewPager;
  final String URL_ROOT = "https://s3-eu-west-1.amazonaws.com/flickrwall/";
  JSONArray remoteImageList;
  int remoteImageIndex = 0;
  private BroadcastReceiver receiverDownloadComplete;
  private HashMap<Long, WallpaperItem> downloadingItems = new HashMap<>();

  private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      // TODO Auto-generated method stub
      // Get extra data included in the Intent
      String localUrl = "file://" + intent.getStringExtra("localUrl");
      long downloadId = intent.getLongExtra("downloadId", 0);
      Log.d("receiver", "Got message: " +  localUrl);
      try {
        WallpaperItem downloadedWallpaperItem = downloadingItems.get(downloadId);
        downloadedWallpaperItem.localFilePath = localUrl;
        adapter.addWallpaperItem(downloadedWallpaperItem);
        downloadingItems.remove(downloadId);
        adapter.notifyDataSetChanged();
      }
      catch (Exception ex) {
        Log.e("image-loading", ex.toString() + " " + ex.getMessage());
      }
    }
  };


  @Override
  public void onResume() {
    super.onResume();
    IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

    this.receiverDownloadComplete = new DownloadReceiver();
    registerReceiver(this.receiverDownloadComplete, intentFilter);
  }

  @Override
  public void onPause() {
    super.onPause();

    unregisterReceiver(this.receiverDownloadComplete);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver, new IntentFilter("file-ready"));

    Log.i("start", "start");
    adapter = new ImagePagerAdapter(MainActivity.this);

    //adapter.addWallpaperItem(new WallpaperItem(R.drawable.ulm, "ulm"));
    //adapter.addWallpaperItem(new WallpaperItem(R.drawable.chiang_mai, "chiang mai"));
    //adapter.addWallpaperItem(new WallpaperItem(R.drawable.himeji, "himeji"));
    //adapter.addWallpaperItem(new WallpaperItem(R.drawable.petronas_twin_tower, "petronas_twin_tower"));
    //setContentView(R.layout.activity_main);

    viewPager = new ViewPager(this);
    viewPager.setId(R.id.view_pager);

    setContentView(viewPager);

    viewPager.setAdapter(adapter);
    viewPager.setOnPageChangeListener(listener);

    getNewImageList();
    downloadImages(3);

  }

  private void getNewImageList() {
    try {
      String url = "https://safe-fjord-67306.herokuapp.com/client-api/get-full-image-list";
      //String url = "http://localhost:4000/sample.json";
      //String url = "http://jsonplaceholder.typicode.com/users";
      remoteImageList = new GetJsonTask().execute(url).get();

      Log.i("json", remoteImageList.toString());
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void downloadImages(int count) {
    if (count < 1 || count > 10) {

      throw new IllegalArgumentException("must download between 1 and 10 images");
    }

    for (int i = 0; i < count; i++) {
      if (remoteImageIndex >= remoteImageList.length()) {
        Log.i("image-loading", "Reached the end of the remote image list. Stopping download attempt.");
        return;
      }
      try {
        ImageDownloader imageDownloader = new ImageDownloader(MainActivity.this);
        JSONObject wallpaperJSONObject = remoteImageList.getJSONObject(remoteImageIndex);
        String downloadUrl = "https://s3-eu-west-1.amazonaws.com/flickrwall/" + wallpaperJSONObject.getString("filename");
        long downloadId = imageDownloader.DownloadImage(downloadUrl);
        downloadingItems.put(downloadId, new WallpaperItem(
                wallpaperJSONObject.getString("title")
        ));
        remoteImageIndex++;

      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }


  ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
            //Log.i("scrolled", i + " " + v + " " + i2);
        }

        @Override
        public void onPageSelected(int i) {
            //Log.i("selected", Integer.toString(i));

          downloadImages(2);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            Log.d("image-pager", "PageScrollStateChanged: " + Integer.toString(i));

        }
    };
}