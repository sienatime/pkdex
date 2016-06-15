package com.siena.pokedex.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.PokemonUtil;
import com.siena.pokedex.R;
import com.siena.pokedex.databinding.RowPokeHeaderBinding;
import com.siena.pokedex.models.AllTypeEfficacy;
import com.siena.pokedex.models.ConsolidatedEncounter;
import com.siena.pokedex.models.Pokemon;
import com.siena.pokedex.models.PokemonType;
import com.siena.pokedex.models.Version;
import com.siena.pokedex.viewModels.PokeInfoHeaderViewModel;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

import static com.siena.pokedex.PokemonUtil.consolidateLevels;
import static com.siena.pokedex.PokemonUtil.getPokeString;
import static com.siena.pokedex.PokemonUtil.getTypeColor;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonInfoAdapter extends BaseAdapter {
  private List<Row> rows = new ArrayList<>();
  private Pokemon pokemon;
  private final int HEADER_ROW = 0;
  private final int SECTION_HEADER_ROW = 1;
  private final int TYPE_EFFICACY_ROW = 2;
  private final int TYPE_ENCOUNTER_ROW = 3;
  private final int TYPE_NO_KNOWN_LOCATIONS_ROW = 4;
  private final int TYPE_VERSION_ROW = 5;
  private Context context;
  private Realm realm;

  public PokemonInfoAdapter(Context context, Pokemon pokemon) {
    this.pokemon = pokemon;
    this.context = context;
    this.realm = Realm.getDefaultInstance();
    setupRows();
  }

  private void setupRows() {
    rows.add(new HeaderRow(HEADER_ROW, pokemon));
    rows.add(new SectionHeaderRow(SECTION_HEADER_ROW, R.string.type_effectiveness));

    AllTypeEfficacy typeEfficacy = AllTypeEfficacy.createAllTypeEfficacy(pokemon.getTypes());

    addTypeEfficacy(typeEfficacy.getWeakTo(), R.string.weak_to);
    addTypeEfficacy(typeEfficacy.getDamagedNormallyBy(), R.string.normal_damage);
    addTypeEfficacy(typeEfficacy.getResistantTo(), R.string.resistant_to);
    addTypeEfficacy(typeEfficacy.getImmuneTo(), R.string.immune_to);

    rows.add(new SectionHeaderRow(SECTION_HEADER_ROW, R.string.locations));

    addEncounterRows(pokemon.getConsolidatedEncounters());
  }

  @Override public int getCount() {
    return rows.size();
  }

  @Override public Row getItem(int position) {
    return rows.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    return rows.get(position).getView(convertView, parent);
  }

  @Override public int getItemViewType(int position) {
    return rows.get(position).getType();
  }

  @Override public int getViewTypeCount() {
    return 6;
  }

  private void addTypeEfficacy(RealmList<PokemonType> types, int stringId) {
    if (types.size() > 0) {
      rows.add(new TypeEfficacyRow(TYPE_EFFICACY_ROW, stringId, context, types));
    }
  }

  private void addEncounterRows(RealmList<ConsolidatedEncounter> encounters) {
    if (encounters.size() > 0) {
      RealmResults<Version> versions = pokemon.getVersions().where().findAllSorted("id");

      for (Version version : versions) {
        Integer versionId = new Integer(version.id);
        rows.add(new VersionHeaderRow(context.getResources(), TYPE_VERSION_ROW, versionId));
        RealmResults<ConsolidatedEncounter> encountersByVersion =
            encounters.where().equalTo("versionId", versionId).findAll();
        for (ConsolidatedEncounter encounter : encountersByVersion) {
          rows.add(new EncounterRow(context.getResources(), TYPE_ENCOUNTER_ROW, encounter, realm));
        }
      }
    } else {
      rows.add(new NoKnownLocationsRow(TYPE_NO_KNOWN_LOCATIONS_ROW));
    }
  }

  public static class HeaderRow implements Row {
    private Pokemon pokemon;
    private int rowType;

    public HeaderRow(int rowType, Pokemon pokemon) {
      this.pokemon = pokemon;
      this.rowType = rowType;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        RowPokeHeaderBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_poke_header, parent, false);
        convertView = binding.getRoot();

        viewHolder = new ViewHolder(binding);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.binding.setViewModel(
          new PokeInfoHeaderViewModel(this.pokemon, parent.getContext()));

      return convertView;
    }

    static class ViewHolder {
      public RowPokeHeaderBinding binding;

      public ViewHolder(RowPokeHeaderBinding binding) {
        this.binding = binding;
      }
    }
  }

  public static class VersionHeaderRow implements Row {
    private int rowType;
    private Integer versionId;
    private Resources res;

    public VersionHeaderRow(Resources res, int rowType, Integer versionId) {
      this.res = res;
      this.rowType = rowType;
      this.versionId = versionId;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        convertView = LayoutInflater.from(PokedexApp.getInstance())
            .inflate(R.layout.version_header, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      int versionNameId = res.getIdentifier("version_name_" + versionId, "string",
          PokedexApp.getInstance().getPackageName());
      viewHolder.versionHeaderTextView.setText(versionNameId);
      viewHolder.versionHeaderTextView.setBackgroundColor(PokemonUtil.getVersionColor(versionId));

      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.version_header) TextView versionHeaderTextView;

      public ViewHolder(View source) {
        ButterKnife.inject(this, source);
      }
    }
  }

  public static class TypeEfficacyRow implements Row {
    private int rowType;
    private int titleId;
    private Context context;
    private RealmList<PokemonType> types;

    public TypeEfficacyRow(int rowType, int titleId, Context context,
        RealmList<PokemonType> types) {
      this.rowType = rowType;
      this.titleId = titleId;
      this.context = context;
      this.types = types;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      LayoutInflater inflater = LayoutInflater.from(PokedexApp.getInstance());
      if (convertView == null) {
        convertView = inflater.inflate(R.layout.row_type_efficacy, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.typeEfficacyLevel.setText(titleId);

      if (viewHolder.typeAnchor.getChildCount() == 0) {
        for (int i = 0; i < types.size(); i++) {
          PokemonType type = types.get(i);
          inflater.inflate(R.layout.textview_type, viewHolder.typeAnchor);
          TextView textView = (TextView) viewHolder.typeAnchor.getChildAt(i);
          textView.setBackgroundColor(getTypeColor(type.getTypeId()));
          textView.setText(getPokeString(type.getTypeId(), "type_").toUpperCase());
        }
      }

      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.type_efficacy_level) TextView typeEfficacyLevel;
      @InjectView(R.id.type_anchor) GridLayout typeAnchor;

      public ViewHolder(View source) {
        ButterKnife.inject(this, source);
      }
    }
  }

  public static class SectionHeaderRow implements Row {
    private int rowType;
    private int titleId;

    public SectionHeaderRow(int rowType, int titleId) {
      this.rowType = rowType;
      this.titleId = titleId;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        convertView = LayoutInflater.from(PokedexApp.getInstance())
            .inflate(R.layout.row_section_header, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.sectionHeaderTextView.setText(titleId);

      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.section_header) TextView sectionHeaderTextView;

      public ViewHolder(View source) {
        ButterKnife.inject(this, source);
      }
    }
  }

  public static class EncounterRow implements Row {
    private Resources res;
    private int rowType;
    private ConsolidatedEncounter encounter;
    private Realm realm;

    public EncounterRow(Resources res, int rowType, ConsolidatedEncounter encounter, Realm realm) {
      this.res = res;
      this.rowType = rowType;
      this.encounter = encounter;
      this.realm = realm;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        convertView = LayoutInflater.from(PokedexApp.getInstance())
            .inflate(R.layout.row_encounter, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.encounterLevels.setText(
          consolidateLevels(encounter.getMinLevel(), encounter.getMaxLevel()));
      viewHolder.encounterLocation.setText(
          getPokeString(encounter.getLocationArea().getLocation().getId(), "location_name_"));
      String locationAreaName =
          getPokeString(encounter.getLocationArea().getId(), "location_area_name_");
      if (locationAreaName != null) {
        viewHolder.encounterLocationArea.setText(locationAreaName);
        viewHolder.encounterLocationArea.setVisibility(View.VISIBLE);
      } else {
        viewHolder.encounterLocationArea.setVisibility(View.GONE);
      }

      String encounterCondition =
          getPokeString(encounter.getEncounterConditionId(), "encounter_condition_");
      if (encounterCondition != null) {
        viewHolder.encounterCondition.setText(encounterCondition);
        viewHolder.encounterCondition.setVisibility(View.VISIBLE);
      } else {
        viewHolder.encounterCondition.setVisibility(View.GONE);
      }

      viewHolder.encounterMethod.setText(
          getPokeString(encounter.getEncounterMethod().getId(), "encounter_method_"));
      viewHolder.encounterRate.setText(String.format(res.getString(R.string.encounter_rate),
          Integer.toString(encounter.getRarity())));

      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.encounter_location) TextView encounterLocation;
      @InjectView(R.id.encounter_location_area) TextView encounterLocationArea;
      @InjectView(R.id.encounter_condition) TextView encounterCondition;
      @InjectView(R.id.encounter_method) TextView encounterMethod;
      @InjectView(R.id.encounter_rate) TextView encounterRate;
      @InjectView(R.id.encounter_levels) TextView encounterLevels;

      public ViewHolder(View source) {
        ButterKnife.inject(this, source);
      }
    }
  }

  public static class NoKnownLocationsRow implements Row {
    private int rowType;

    public NoKnownLocationsRow(int rowType) {
      this.rowType = rowType;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(PokedexApp.getInstance())
            .inflate(R.layout.row_no_known_locations, parent, false);
      }

      return convertView;
    }
  }
}
