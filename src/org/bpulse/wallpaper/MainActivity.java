package org.bpulse.wallpaper;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.facebook.ads.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends Activity implements InterstitialAdListener {
  ImagePagerAdapter adapter;
  ViewPager viewPager;
  final String URL_ROOT = "https://s3-eu-west-1.amazonaws.com/flickrwall/";
  JSONArray remoteImageList;
  int remoteImageIndex = 0;
  private BroadcastReceiver receiverDownloadComplete;
  private HashMap<Long, WallpaperItem> downloadingItems = new HashMap<>();
  Boolean interstitialShown = false;

  SharedPreferences sharedPrefs;
  SharedPreferences.Editor sharedPrefsEditor;

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
        adapter.notifyDataSetChanged();

        sharedPrefsEditor.putString(downloadedWallpaperItem.id, downloadedWallpaperItem.toString());
        HashSet<String> savedIds = getSavedIds();
        savedIds.add(downloadedWallpaperItem.id);
        sharedPrefsEditor.putStringSet("idlist", savedIds);
        sharedPrefsEditor.commit();
        downloadingItems.remove(downloadId);

        if (! interstitialShown) {
          loadInterstitialAd();
        }
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


    // test device
    AdSettings.addTestDevice("a9ad04769af2533098efea8a105e0882");
    AdSettings.addTestDevice("da2494b357f7d84a37b2f383f3d33c14");
    AdSettings.addTestDevice("f7457aa5dc8312f9e39ff42a35b05ea0");
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

    viewPager = new ViewPager(this);
    viewPager.setId(R.id.view_pager);

    setContentView(viewPager);

    viewPager.setAdapter(adapter);
    viewPager.setOnPageChangeListener(listener);

    sharedPrefs = getSharedPreferences("flickrwall", MODE_PRIVATE);
    sharedPrefsEditor = sharedPrefs.edit();

    loadFromSharedPreferences();

    if (getNewImageList()) {
      downloadImages(3);
    }
  }

  private void loadFromSharedPreferences() {
    HashSet<String> savedIds = getSavedIds();
    for (String s : savedIds) {
      WallpaperItem  wallpaperItem = WallpaperItem.Parse(sharedPrefs.getString(s, ""));
      adapter.addWallpaperItem(wallpaperItem);
      adapter.notifyDataSetChanged();
    }

  }

  private Boolean getNewImageList() {
    try {
      String url = "https://safe-fjord-67306.herokuapp.com/client-api/get-full-image-list";
      remoteImageList = new GetJsonTask().execute(url).get();

      Log.i("json", remoteImageList.toString());

      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
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

        HashSet<String> savedIds = getSavedIds();
        if (savedIds.contains(wallpaperJSONObject.getString("id"))) {
          Log.i("image-loading", "This id is already loaded: " + wallpaperJSONObject.getString("id") + ", trying next.");
          remoteImageIndex++;
          return;
        }

        String downloadUrl = "https://s3-eu-west-1.amazonaws.com/flickrwall/" + wallpaperJSONObject.getString("filename");
        long downloadId = imageDownloader.DownloadImage(downloadUrl);
        downloadingItems.put(downloadId, new WallpaperItem(
                wallpaperJSONObject.getString("id"),
                wallpaperJSONObject.getString("title"),
                wallpaperJSONObject.getString("ownerrealname"),
                wallpaperJSONObject.getString("ownerusername"),
                wallpaperJSONObject.getString("photo_page_url")
        ));
        remoteImageIndex++;

      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  private HashSet<String> getSavedIds() {
    return (HashSet<String>)sharedPrefs.getStringSet("idlist", new HashSet<String>());
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
            //Log.d("image-pager", "PageScrollStateChanged: " + Integer.toString(i));

        }
    };



  private InterstitialAd interstitialAd;

  private void loadInterstitialAd(){
    Log.i("ads", "loadInterstitialAd called");


    interstitialAd = new InterstitialAd(this, "288062771531358_288064571531178");
    interstitialAd.setAdListener(MainActivity.this);
    interstitialAd.loadAd();
    Log.i("ads", "loadInterstitialAd finished");

  }

  @Override
  public void onInterstitialDisplayed(Ad ad) {
    interstitialShown = true;
  }

  @Override
  public void onInterstitialDismissed(Ad ad) {

  }

  @Override
  public void onError(Ad ad, AdError adError) {

  }

  @Override
  public void onAdLoaded(Ad ad) {
    Log.i("ads", "onAdLoaded called");
    interstitialAd.show();
  }

  @Override
  public void onAdClicked(Ad ad) {

  }
}