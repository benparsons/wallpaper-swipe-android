package com.sqisland.android.swipe_image_viewer;

import android.app.Activity;
import android.content.Context;
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

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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