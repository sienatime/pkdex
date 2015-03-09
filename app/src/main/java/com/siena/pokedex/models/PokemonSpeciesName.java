package com.siena.pokedex.models;

import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 2/28/15.
 */
public class PokemonSpeciesName extends RealmObject {
  private int pokemonSpeciesId, localLanguageId;
  private String name, genus;

  public int getPokemonSpeciesId() {
    return pokemonSpeciesId;
  }

  public void setPokemonSpeciesId(int pokemonSpeciesId) {
    this.pokemonSpeciesId = pokemonSpeciesId;
  }

  public int getLocalLanguageId() {
    return localLanguageId;
  }

  public void setLocalLanguageId(int localLanguageId) {
    this.localLanguageId = localLanguageId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGenus() {
    return genus;
  }

  public void setGenus(String genus) {
    this.genus = genus;
  }
}
