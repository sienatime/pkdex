package com.siena.pokedex.models;

import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 3/14/15.
 */
public class LocationName extends RealmObject {
  // location_id	local_language_id	name
  private int locationId, localLanguageId;
  private String name;

  public LocationName() {
  }

  public int getLocationId() {
    return locationId;
  }

  public void setLocationId(int locationId) {
    this.locationId = locationId;
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
