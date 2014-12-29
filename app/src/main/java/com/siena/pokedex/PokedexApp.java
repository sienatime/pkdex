package com.siena.pokedex;

import android.app.Application;
import dagger.ObjectGraph;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class PokedexApp extends Application {
  private ObjectGraph graph;
  private static PokedexApp instance;

  public static PokedexApp getInstance() {
    return instance;
  }

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
    graph = ObjectGraph.create(getModules().toArray());
    inject(this);
  }

  public void inject(Object object) {
    graph.inject(object);
  }

  protected List<Object> getModules() {
    return Arrays.<Object>asList(new PokedexModule());
  }

}
