package com.siena.pokedex.models;

/**
 * Created by Siena Aguayo on 1/1/15.
 */
public class Encounter {
  private int id, pokemonId, versionId, locationAreaId, minLevel, maxLevel, rarity,
      encounterConditionId, encounterMethodId;

  public Encounter(int id, int pokemonId, int versionId, int locationAreaId, int minLevel, int maxLevel,
      int rarity, int encounterConditionId, int encounterMethodId) {
    this.id = id;
    this.pokemonId = pokemonId;
    this.versionId = versionId;
    this.locationAreaId = locationAreaId;
    this.minLevel = minLevel;
    this.maxLevel = maxLevel;
    this.rarity = rarity;
    this.encounterConditionId = encounterConditionId;
    this.encounterMethodId = encounterMethodId;
  }

  public int getId() {
    return id;
  }

  public int getPokemonId() {
    return pokemonId;
  }

  public int getVersionId() {
    return versionId;
  }

  public int getLocationAreaId() {
    return locationAreaId;
  }

  public int getMinLevel() {
    return minLevel;
  }

  public int getMaxLevel() {
    return maxLevel;
  }

  public int getRarity() {
    return rarity;
  }

  public int getEncounterConditionId() {
    return encounterConditionId;
  }

  public int getEncounterMethodId() {
    return encounterMethodId;
  }
}
