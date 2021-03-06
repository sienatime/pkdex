package com.siena.pokedex.viewModels.show;

import android.content.res.Resources;
import android.view.View;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.R;
import com.siena.pokedex.models.persisted.ConsolidatedEncounter;

import static com.siena.pokedex.ResourceUtil.getStringForIdentifier;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class EncounterViewModel {
  public String location;
  public String locationAreaName;
  public int locationAreaVisibility;
  public String encounterCondition;
  public int encounterConditionVisibility;
  public String encounterMethod;
  public String encounterRate;
  public String encounterLevels;

  public EncounterViewModel(ConsolidatedEncounter encounter) {
    Resources res = PokedexApp.getInstance().getResources();

    this.location = getStringForIdentifier("location_name_",
        encounter.getLocationArea().getLocation().getId());
    this.locationAreaName = getStringForIdentifier("location_area_name_",
        encounter.getLocationArea().getId());
    this.locationAreaVisibility = locationAreaName != null ? View.VISIBLE : View.GONE;

    this.encounterCondition = getStringForIdentifier("encounter_condition_",
        encounter.getEncounterConditionId());
    this.encounterConditionVisibility = encounterCondition != null ? View.VISIBLE : View.GONE;

    this.encounterMethod = getStringForIdentifier("encounter_method_",
        encounter.getEncounterMethod().getId());
    this.encounterRate = String.format(res.getString(R.string.encounter_rate),
        Integer.toString(encounter.getRarity()));
    this.encounterLevels = consolidateLevels(encounter.getMinLevel(), encounter.getMaxLevel());
  }

  public static String consolidateLevels(int minLevel, int maxLevel) {
    Resources res = PokedexApp.getInstance().getResources();
    if (minLevel == maxLevel) {
      return String.format(res.getString(R.string.level_singular), Integer.toString(minLevel));
    } else {
      return String.format(res.getString(R.string.level_range), Integer.toString(minLevel),
          Integer.toString(maxLevel));
    }
  }
}
