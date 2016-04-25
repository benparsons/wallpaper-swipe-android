package org.bpulse.wallpaper;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
  ImagePagerAdapter adapter;
  ViewPager viewPager;
  final String URL_ROOT = "https://s3-eu-west-1.amazonaws.com/flickrwall/";
  private BroadcastReceiver receiverDownloadComplete;

  @Override
  public void onResume() {
    super.onResume();
    IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

    //receiverDownloadComplete
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
Log.i("start", "start");
    adapter = new ImagePagerAdapter(MainActivity.this);

    adapter.addWallpaperItem(new WallpaperItem(R.drawable.ulm, "ulm"));
    adapter.addWallpaperItem(new WallpaperItem(R.drawable.chiang_mai, "chiang mai"));
    adapter.addWallpaperItem(new WallpaperItem(R.drawable.himeji, "himeji"));
    adapter.addWallpaperItem(new WallpaperItem(R.drawable.petronas_twin_tower, "petronas_twin_tower"));
    //setContentView(R.layout.activity_main);

    viewPager = new ViewPager(this);
    viewPager.setId(R.id.view_pager);

    setContentView(viewPager);

    viewPager.setAdapter(adapter);
    viewPager.setOnPageChangeListener(listener);

    getNewImageList();
    ImageDownloader imageDownloader = new ImageDownloader(MainActivity.this);
    imageDownloader.DownloadImage("https://s3-eu-west-1.amazonaws.com/flickrwall/f947560e-d28b-488a-aceb-c1ec0eae5eab.jpg");
  }

  private void getNewImageList() {
    JSONObject json = new JSONObject();
    try {
      String url = "http://safe-fjord-67306.herokuapp.com/demo_response.json";
      //String url = "http://localhost:4000/sample.json";
      //String url = "http://jsonplaceholder.typicode.com/users";
      json = new GetJsonTask().execute(url).get();

      Log.i("json", json.toString());
    }
    catch(Exception e)
    {
      e.printStackTrace();
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

            JSONObject json = new JSONObject();
            adapter.notifyDataSetChanged();
            try {
                String url = "http://safe-fjord-67306.herokuapp.com/sample.json";
                //String url = "http://localhost:4000/sample.json";
                //String url = "http://jsonplaceholder.typicode.com/users";
                json = new GetJsonTask().execute(url).get();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            Log.i("json", json.toString());
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            Log.i("statechanged", Integer.toString(i));

        }
    };
}