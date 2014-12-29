package com.siena.pokedex;

import android.app.Application;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class PokedexApp extends Application {
  private static PokedexApp instance;

  public static PokedexApp getInstance() {
    return instance;
  }

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
  }
}
