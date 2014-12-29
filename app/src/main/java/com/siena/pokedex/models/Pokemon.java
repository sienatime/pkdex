package com.siena.pokedex.models;

import java.util.List;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class Pokemon {
  private int id;
  //_id|identifier|species_id|height|weight|base_experience|order|is_default
  private String name;
  private String genus;
  private List<Type> types; //localized

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

  public List<Type> getTypes() {
    return types;
  }

  public void setTypes(List<Type> types) {
    this.types = types;
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

  public String nameToKey() {
    return name.replace("-", "_");
  }

  public String getImageName() {
    return "sprite_" + Integer.toString(id);
  }

  public static class Type {
    private Integer id;
    private String localizedName;

    public Type(Integer id, String localizedName) {
      this.id = id;
      this.localizedName = localizedName;
    }

    public Integer getId() {
      return id;
    }

    public String getLocalizedName() {
      return localizedName;
    }
  }
}
