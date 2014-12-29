package com.siena.pokedex.models;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class Pokemon {
  private int id;
  //_id|identifier|species_id|height|weight|base_experience|order|is_default
  private String name;

  public Pokemon(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
