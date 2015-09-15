package com.siena.pokedex.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 9/14/15.
 */
public class Version extends RealmObject {
  private int id, pokemonId;
  private RealmList<Location> locations;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPokemonId() {
    return pokemonId;
  }

  public void setPokemonId(int pokemonId) {
    this.pokemonId = pokemonId;
  }

  public RealmList<Location> getLocations() {
    return locations;
  }

  public void setLocations(RealmList<Location> locations) {
    this.locations = locations;
  }
}
