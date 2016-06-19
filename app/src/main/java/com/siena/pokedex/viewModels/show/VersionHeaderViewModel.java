package com.siena.pokedex.viewModels.show;

import android.content.res.Resources;
import com.siena.pokedex.PokedexApp;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class VersionHeaderViewModel {
  public int versionNameId;
  public int color;

  public VersionHeaderViewModel(int versionId) {
    Resources res = PokedexApp.getInstance().getResources();
    this.versionNameId = res.getIdentifier("version_name_" + versionId, "string",
        PokedexApp.getInstance().getPackageName());
    this.color = getVersionColor(versionId);
  }

  public static int getVersionColor(Integer id) {
    PokedexApp app = PokedexApp.getInstance();
    Resources res = app.getResources();
    int colorId = res.getIdentifier("version" + id, "color", app.getPackageName());
    return res.getColor(colorId);
  }
}
