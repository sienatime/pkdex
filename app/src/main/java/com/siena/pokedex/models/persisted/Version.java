package com.siena.pokedex.models.persisted;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 9/14/15.
 */
public class Version extends RealmObject {
  public int id, pokemonId;
  public RealmList<Location> locations;
}
