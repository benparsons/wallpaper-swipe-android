package com.sqisland.android.swipe_image_viewer;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    public List<WallpaperItem> mImages2 = new ArrayList<>();

    public ImagePagerAdapter(Context _context) {
        context = _context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImages2.add(new WallpaperItem(R.drawable.chiang_mai, "chiang mai"));
        mImages2.add(new WallpaperItem(R.drawable.chiang_mai, "chiang mai"));
    }

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

        if (position > 3) {
            View view = inflater.inflate(R.layout.simple_text, null);
            ((ViewPager) container).addView(view, 0);
            return view;
        }

        final View wallpaperPage = inflater.inflate(R.layout.wallpaper_page, null);
        ImageView imageView = (ImageView)wallpaperPage.findViewById(R.id.ivWallpaper);

        int padding = context.getResources().getDimensionPixelSize(
                R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        final WallpaperItem wallpaperItem = mImages2.get(position);
        imageView.setImageResource(wallpaperItem.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ui", "image clicked");
                TableLayout table = (TableLayout)wallpaperPage.findViewById(R.id.tableDetails);
                table.setVisibility(View.VISIBLE);
            }
        });
        TextView textView = (TextView)wallpaperPage.findViewById(R.id.tvTitle);
        textView.setText(wallpaperItem.title);
        textView.setTextColor(Color.RED);

        ((ViewPager) container).addView(wallpaperPage, 0);
        return wallpaperPage;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i("destroy", Integer.toString(position));
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}