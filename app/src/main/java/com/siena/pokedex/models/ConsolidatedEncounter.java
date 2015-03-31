package com.siena.pokedex.models;

import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 1/1/15.
 */
public class ConsolidatedEncounter extends RealmObject {
  private int versionId, pokemonId, minLevel, maxLevel, rarity, encounterMethodId,
      encounterConditionId, locationAreaId;
  private LocationArea locationArea;

  public ConsolidatedEncounter() {
  }

  public int getVersionId() {
    return versionId;
  }

  public void setVersionId(int versionId) {
    this.versionId = versionId;
  }

  public LocationArea getLocationArea() {
    return locationArea;
  }

  public void setLocationArea(LocationArea locationArea) {
    this.locationArea = locationArea;
  }

  public int getLocationAreaId() {
    return locationAreaId;
  }

  public void setLocationAreaId(int locationAreaId) {
    this.locationAreaId = locationAreaId;
  }

  public int getPokemonId() {
    return pokemonId;
  }

  public void setPokemonId(int pokemonId) {
    this.pokemonId = pokemonId;
  }

  public int getEncounterMethodId() {
    return encounterMethodId;
  }

  public void setEncounterMethodId(int encounterMethodId) {
    this.encounterMethodId = encounterMethodId;
  }

  public int getEncounterConditionId() {
    return encounterConditionId;
  }

  public void setEncounterConditionId(int encounterConditionId) {
    this.encounterConditionId = encounterConditionId;
  }

  public int getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(int minLevel) {
    this.minLevel = minLevel;
  }

  public int getMaxLevel() {
    return maxLevel;
  }

  public void setMaxLevel(int maxLevel) {
    this.maxLevel = maxLevel;
  }

  public int getRarity() {
    return rarity;
  }

  public void setRarity(int rarity) {
    this.rarity = rarity;
  }
}
