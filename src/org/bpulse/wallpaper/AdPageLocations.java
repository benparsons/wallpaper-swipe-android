package org.bpulse.wallpaper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 03/07/2016.
 */
public class AdPageLocations {
  public List<Integer> SpecificPages = new ArrayList<Integer>();

  public AdPageLocations() {
    for (int i = 0; i < 500; i++) {
      if (i > 0 && i % 5 == 0) {
        SpecificPages.add(i);
      }
    }
  }
}
