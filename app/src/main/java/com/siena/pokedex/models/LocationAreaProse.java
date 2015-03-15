package com.siena.pokedex.models;

import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 3/14/15.
 */
public class LocationAreaProse extends RealmObject {
  private int locationAreaId, localLanguageId;
  private String name;

  public LocationAreaProse() {
  }

  public int getLocationAreaId() {
    return locationAreaId;
  }

  public void setLocationAreaId(int locationAreaId) {
    this.locationAreaId = locationAreaId;
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
}
