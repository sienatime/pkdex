package com.siena.pokedex.models.persisted;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Siena Aguayo on 3/14/15.
 */
public class LocationArea extends RealmObject {
  //"location_areas.id, location_areas.location_id, location_areas.game_index, "
  //    + "location_areas.identifier
  @PrimaryKey private int id;
  private int locationId, gameIndex;
  private String identifier;
  private Location location;

  public LocationArea() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getLocationId() {
    return locationId;
  }

  public void setLocationId(int locationId) {
    this.locationId = locationId;
  }

  public int getGameIndex() {
    return gameIndex;
  }

  public void setGameIndex(int gameIndex) {
    this.gameIndex = gameIndex;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }
}
