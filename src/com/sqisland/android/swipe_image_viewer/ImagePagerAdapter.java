package com.sqisland.android.swipe_image_viewer;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    Context context;
    public List<WallpaperItem> mImages2 = new ArrayList<>();

    public ImagePagerAdapter(Context _context) {
        context = _context;
        mImages2.add(new WallpaperItem(R.drawable.chiang_mai, "chiang mai"));
        mImages2.add(new WallpaperItem(R.drawable.chiang_mai, "chiang mai"));
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
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i("position:", Integer.toString(position));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if (position > 3) {
            TextView title = new TextView(context);
            title.setText("test text");
            title.setTextColor(Color.WHITE);
            linearLayout.addView(title);
            ((ViewPager) container).addView(linearLayout, 0);
            return linearLayout;
        }

        ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(
                R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        WallpaperItem wallpaperItem = mImages2.get(position);
        imageView.setImageResource(wallpaperItem.image);
        linearLayout.addView(imageView);
        TextView textView = new TextView(context);
        textView.setText(wallpaperItem.title);
        textView.setTextColor(Color.RED);
        linearLayout.addView(textView);
        ((ViewPager) container).addView(linearLayout, 0);
        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i("destroy", Integer.toString(position));
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}