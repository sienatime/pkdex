package com.siena.pokedex.models;

/**
 * Created by Siena Aguayo on 1/1/15.
 */
public class Encounter {
  private int versionId;
  private String locationName;
  private String method;
  private String levelRange;
  private int rate;
  private String areaName;
  private String condition;

  public Encounter(int versionId, String locationName, String method, String levelRange, int rate,
      String areaName) {
    this.versionId = versionId;
    this.locationName = locationName;
    this.method = method;
    this.levelRange = levelRange;
    this.rate = rate;
    this.areaName = areaName;
  }

  public int getVersionId() {
    return versionId;
  }

  public String getLocationName() {
    return locationName;
  }

  public int getRate() {
    return rate;
  }

  public String getLevelRange() {
    return levelRange;
  }

  public String getMethod() {
    return method;
  }

  public String getAreaName() {
    return areaName;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }
}
