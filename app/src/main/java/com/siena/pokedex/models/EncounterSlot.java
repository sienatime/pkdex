package com.siena.pokedex.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Siena Aguayo on 3/25/15.
 */
public class EncounterSlot extends RealmObject {
  // _id	version_group_id	encounter_method_id	slot	rarity
  @PrimaryKey private int id;
  private int versionGroupId, encounterMethodId, slot, rarity;

  public EncounterSlot() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getVersionGroupId() {
    return versionGroupId;
  }

  public void setVersionGroupId(int versionGroupId) {
    this.versionGroupId = versionGroupId;
  }

  public int getEncounterMethodId() {
    return encounterMethodId;
  }

  public void setEncounterMethodId(int encounterMethodId) {
    this.encounterMethodId = encounterMethodId;
  }

  public int getSlot() {
    return slot;
  }

  public void setSlot(int slot) {
    this.slot = slot;
  }

  public int getRarity() {
    return rarity;
  }

  public void setRarity(int rarity) {
    this.rarity = rarity;
  }
}
