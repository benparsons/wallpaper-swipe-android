package org.bpulse.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    private List<WallpaperItem> mImages2 = new ArrayList<>();

    public ImagePagerAdapter(Context _context) {
        context = _context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addWallpaperItem(WallpaperItem wallpaperItem) {
        mImages2.add(wallpaperItem);
    }

    @Override
    public int getCount() {
        return mImages2.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("image-pager", "position:" +Integer.toString(position));

        if (position > 10) {
            View view = inflater.inflate(R.layout.native_ad_page, null);
            ((ViewPager) container).addView(view, 0);
            return view;
        }

        final View wallpaperPage = inflater.inflate(R.layout.wallpaper_page, null);
        ImageView imageView = (ImageView)wallpaperPage.findViewById(R.id.ivWallpaper);

        final WallpaperItem wallpaperItem = mImages2.get(position);

      if (wallpaperItem.localFilePath != null) {
        imageView.setImageURI(Uri.parse(wallpaperItem.localFilePath));
      }
      else {
        imageView.setImageResource(wallpaperItem.image);
      }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout table = (TableLayout)wallpaperPage.findViewById(R.id.tableDetails);
                table.setVisibility(table.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            }
        });
        TextView textView = (TextView)wallpaperPage.findViewById(R.id.tvTitle);
        textView.setText(wallpaperItem.title);
        textView.setTextColor(Color.RED);

        enableSetWallpaperButton(wallpaperPage, wallpaperItem);

        ((ViewPager) container).addView(wallpaperPage, 0);
        return wallpaperPage;
    }

    private void enableSetWallpaperButton(View wallpaperPage, final WallpaperItem wallpaperItem) {
        Button btnSetWallpaper = (Button)wallpaperPage.findViewById(R.id.btnSetWallpaper);
        btnSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
                try {
                  InputStream ins = new URL(wallpaperItem.localFilePath).openStream();
                  myWallpaperManager.setStream(ins);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("image-pager", "destroy: " + Integer.toString(position));
        ((ViewPager) container).removeView((FrameLayout) object);
    }
}