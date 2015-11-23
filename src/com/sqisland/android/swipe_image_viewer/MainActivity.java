package com.sqisland.android.swipe_image_viewer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    ImagePagerAdapter adapter = new ImagePagerAdapter();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

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
            Log.i("selected", Integer.toString(i));
            adapter.mImages2.add(R.drawable.ulm);
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onPageScrollStateChanged(int i) {
            Log.i("statechanged", Integer.toString(i));

        }
    };

  private class ImagePagerAdapter extends PagerAdapter {
      private List<Integer> mImages2 = new ArrayList<>();

      public ImagePagerAdapter() {
          mImages2.add(R.drawable.chiang_mai);
          mImages2.add(R.drawable.chiang_mai);
      }


    private int[] mImages = new int[] {
        R.drawable.chiang_mai,
        R.drawable.himeji,
        R.drawable.petronas_twin_tower,
        R.drawable.ulm
    };

    @Override
    public int getCount() {
      return mImages2.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      Context context = MainActivity.this;
      ImageView imageView = new ImageView(context);
      int padding = context.getResources().getDimensionPixelSize(
          R.dimen.padding_medium);
      imageView.setPadding(padding, padding, padding, padding);
      imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      imageView.setImageResource(mImages2.get(position));
      ((ViewPager) container).addView(imageView, 0);
      return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i("destroy", Integer.toString(position));
      ((ViewPager) container).removeView((ImageView) object);
    }
  }
}