package com.siena.pokedex.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Siena Aguayo on 1/1/15.
 */
public class Encounter extends RealmObject {
  @PrimaryKey private int id;
  private int versionId, encounterSlotId, minLevel, maxLevel;
  //private LocationArea locationArea;
  //private LocationName locationName;

  public Encounter() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getVersionId() {
    return versionId;
  }

  public void setVersionId(int versionId) {
    this.versionId = versionId;
  }

  public int getEncounterSlotId() {
    return encounterSlotId;
  }

  public void setEncounterSlotId(int encounterSlotId) {
    this.encounterSlotId = encounterSlotId;
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
}
