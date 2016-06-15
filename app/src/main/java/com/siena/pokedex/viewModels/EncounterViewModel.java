package com.siena.pokedex.viewModels;

import android.content.res.Resources;
import android.view.View;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.R;
import com.siena.pokedex.models.ConsolidatedEncounter;

import static com.siena.pokedex.PokemonUtil.consolidateLevels;
import static com.siena.pokedex.PokemonUtil.getPokeString;

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

    this.location = getPokeString(encounter.getLocationArea().getLocation().getId(), "location_name_");
    this.locationAreaName = getPokeString(encounter.getLocationArea().getId(), "location_area_name_");
    this.locationAreaVisibility = locationAreaName != null ? View.VISIBLE : View.GONE;

    this.encounterCondition = getPokeString(encounter.getEncounterConditionId(), "encounter_condition_");
    this.encounterConditionVisibility = encounterCondition != null ? View.VISIBLE : View.GONE;

    this.encounterMethod = getPokeString(encounter.getEncounterMethod().getId(), "encounter_method_");
    this.encounterRate = String.format(res.getString(R.string.encounter_rate),
        Integer.toString(encounter.getRarity()));
    this.encounterLevels = consolidateLevels(encounter.getMinLevel(), encounter.getMaxLevel());
  }
}
