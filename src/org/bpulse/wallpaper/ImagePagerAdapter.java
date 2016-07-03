package org.bpulse.wallpaper;

import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
  Context context;
  LayoutInflater inflater;
  private List<WallpaperItem> mImages2 = new ArrayList<>();
  private AdPageLocations adPageLocations = new AdPageLocations();

  private NativeAd nativeAd;

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

      if (adPageLocations.SpecificPages.contains(position)) {

        final View nativeAdPage = inflater.inflate(R.layout.native_ad_page, null);

        nativeAd = new NativeAd(context, "288062771531358_288063254864643");
        nativeAd.setAdListener(new AdListener() {
          @Override
          public void onError(Ad ad, AdError adError) {

          }

          @Override
          public void onAdLoaded(Ad ad) {
            if (ad != nativeAd) {
              return;
            }

            String titleForAd = nativeAd.getAdTitle();
            String socialContextForAd = nativeAd.getAdSocialContext();
            String titleForAdButton = nativeAd.getAdCallToAction();
            String textForAdBody = nativeAd.getAdBody();

            // title
            TextView tvTitle = (TextView) nativeAdPage.findViewById(R.id.tvAdTitle);
            tvTitle.setText(titleForAd);

            // Ad body
            TextView nativeAdBody = (TextView)nativeAdPage.findViewById(R.id.native_ad_body);
            nativeAdBody.setText(textForAdBody);

            // Downloading and setting the ad icon.
            ImageView nativeAdIcon = (ImageView)nativeAdPage.findViewById(R.id.native_ad_icon);
            NativeAd.Image adIcon = nativeAd.getAdIcon();
            NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

            // Download and setting the cover image.
            MediaView nativeAdMedia = (MediaView)nativeAdPage.findViewById(R.id.native_ad_media);
            nativeAdMedia.setNativeAd(nativeAd);

            // AdChoices
            AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
            ((LinearLayout)nativeAdPage.findViewById(R.id.llTopLine)).addView(adChoicesView, 0);
          }

          @Override
          public void onAdClicked(Ad ad) {

          }
        });
        nativeAd.loadAd();

        ((ViewPager) container).addView(nativeAdPage, 0);
        return nativeAdPage;
      }

      int realPosition = 0;
      for (int i = 0; i < position; i++){
        if (! adPageLocations.SpecificPages.contains(i)) {
          realPosition++;
        }
      }

      final View wallpaperPage = inflater.inflate(R.layout.wallpaper_page, null);
      ImageView imageView = (ImageView)wallpaperPage.findViewById(R.id.ivWallpaper);

      final WallpaperItem wallpaperItem = mImages2.get(realPosition);

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
      TextView tvTitle = (TextView)wallpaperPage.findViewById(R.id.tvTitle);
      tvTitle.setText(wallpaperItem.title);

      if (wallpaperItem.photographer != null && wallpaperItem.photographer.length() > 0) {
        TextView tvPhotographer = (TextView) wallpaperPage.findViewById(R.id.tvPhotographer);
        tvPhotographer.setText(wallpaperItem.photographer);

        wallpaperPage.findViewById(R.id.trUsernameRow).setVisibility(View.GONE);
      }
      else {
        wallpaperPage.findViewById(R.id.trPhotographerRow).setVisibility(View.GONE);

        TextView tvUsername = (TextView)wallpaperPage.findViewById(R.id.tvUsername);
        tvUsername.setText(wallpaperItem.username);
      }
      enableOpenFlickrButton(wallpaperPage, wallpaperItem);

      enableSetWallpaperButton(wallpaperPage, wallpaperItem);

      ((ViewPager) container).addView(wallpaperPage, 0);
      return wallpaperPage;
    }

  private void enableOpenFlickrButton(View wallpaperPage, final WallpaperItem wallpaperItem) {
    Button btnFlickr = (Button)wallpaperPage.findViewById(R.id.btnFlickr);
    btnFlickr.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(Intent.ACTION_VIEW,wallpaperItem.getPhotoPage());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
          context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
          // Chrome browser presumably not installed so allow user to choose instead
          intent.setPackage(null);
          context.startActivity(intent);
        }
      }
    });
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