package com.siena.pokedex.adapters;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.R;
import com.siena.pokedex.databinding.RowShowEncounterBinding;
import com.siena.pokedex.databinding.RowShowHeaderBinding;
import com.siena.pokedex.databinding.RowShowSectionHeaderBinding;
import com.siena.pokedex.databinding.RowShowTypeEfficacyBinding;
import com.siena.pokedex.databinding.RowShowVersionHeaderBinding;
import com.siena.pokedex.models.AllTypeEfficacy;
import com.siena.pokedex.models.persisted.ConsolidatedEncounter;
import com.siena.pokedex.models.persisted.Pokemon;
import com.siena.pokedex.models.persisted.PokemonType;
import com.siena.pokedex.models.persisted.Version;
import com.siena.pokedex.viewModels.show.EncounterViewModel;
import com.siena.pokedex.viewModels.show.HeaderViewModel;
import com.siena.pokedex.viewModels.show.SectionHeaderViewModel;
import com.siena.pokedex.viewModels.show.TypeEfficacyViewModel;
import com.siena.pokedex.viewModels.show.VersionHeaderViewModel;
import io.realm.RealmList;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonShowAdapter extends BaseAdapter {
  private List<Row> rows = new ArrayList<>();
  private Pokemon pokemon;
  private final int HEADER_ROW = 0;
  private final int SECTION_HEADER_ROW = 1;
  private final int TYPE_EFFICACY_ROW = 2;
  private final int TYPE_ENCOUNTER_ROW = 3;
  private final int TYPE_NO_KNOWN_LOCATIONS_ROW = 4;
  private final int TYPE_VERSION_ROW = 5;

  public PokemonShowAdapter(Pokemon pokemon) {
    this.pokemon = pokemon;
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
      rows.add(new TypeEfficacyRow(TYPE_EFFICACY_ROW, stringId, types));
    }
  }

  private void addEncounterRows(RealmList<ConsolidatedEncounter> encounters) {
    if (encounters.size() > 0) {
      RealmResults<Version> versions = pokemon.getVersions().where().findAllSorted("id");

      for (Version version : versions) {
        Integer versionId = new Integer(version.id);
        rows.add(new VersionHeaderRow(TYPE_VERSION_ROW, versionId));
        RealmResults<ConsolidatedEncounter> encountersByVersion =
            encounters.where().equalTo("versionId", versionId).findAll();
        for (ConsolidatedEncounter encounter : encountersByVersion) {
          rows.add(new EncounterRow(TYPE_ENCOUNTER_ROW, encounter));
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
        RowShowHeaderBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_show_header, parent, false);
        convertView = binding.getRoot();

        viewHolder = new ViewHolder(binding);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.binding.setViewModel(
          new HeaderViewModel(this.pokemon, parent.getContext()));

      return convertView;
    }

    static class ViewHolder {
      public RowShowHeaderBinding binding;

      public ViewHolder(RowShowHeaderBinding binding) {
        this.binding = binding;
      }
    }
  }

  public static class VersionHeaderRow implements Row {
    private int rowType;
    private Integer versionId;

    public VersionHeaderRow(int rowType, Integer versionId) {
      this.rowType = rowType;
      this.versionId = versionId;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        RowShowVersionHeaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
            R.layout.row_show_version_header, parent, false);
        convertView = binding.getRoot();
        viewHolder = new ViewHolder(binding);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.binding.setViewModel(new VersionHeaderViewModel(versionId));

      return convertView;
    }

    static class ViewHolder {
      RowShowVersionHeaderBinding binding;

      public ViewHolder(RowShowVersionHeaderBinding binding) {
        this.binding = binding;
      }
    }
  }

  public static class TypeEfficacyRow implements Row {
    private int rowType;
    private int titleId;
    private RealmList<PokemonType> types;

    public TypeEfficacyRow(int rowType, int titleId, RealmList<PokemonType> types) {
      this.rowType = rowType;
      this.titleId = titleId;
      this.types = types;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        RowShowTypeEfficacyBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_show_type_efficacy, parent, false);
        convertView = binding.getRoot();
        viewHolder = new ViewHolder(binding);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.binding.setViewModel(new TypeEfficacyViewModel(convertView, titleId, types));

      return convertView;
    }

    static class ViewHolder {
      public RowShowTypeEfficacyBinding binding;

      public ViewHolder(RowShowTypeEfficacyBinding binding) {
        this.binding = binding;
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
        RowShowSectionHeaderBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_show_section_header, parent, false);
        convertView = binding.getRoot();
        viewHolder = new ViewHolder(binding);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.binding.setViewModel(new SectionHeaderViewModel(titleId));

      return convertView;
    }

    static class ViewHolder {
      public RowShowSectionHeaderBinding binding;

      public ViewHolder(RowShowSectionHeaderBinding binding) {
        this.binding = binding;
      }
    }
  }

  public static class EncounterRow implements Row {
    private int rowType;
    private ConsolidatedEncounter encounter;

    public EncounterRow(int rowType, ConsolidatedEncounter encounter) {
      this.rowType = rowType;
      this.encounter = encounter;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        RowShowEncounterBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_show_encounter, parent, false);
        convertView = binding.getRoot();
        viewHolder = new ViewHolder(binding);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.binding.setViewModel(new EncounterViewModel(encounter));

      return convertView;
    }

    static class ViewHolder {
      RowShowEncounterBinding binding;

      public ViewHolder(RowShowEncounterBinding binding) {
        this.binding = binding;
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
            .inflate(R.layout.row_show_no_known_locations, parent, false);
      }

      return convertView;
    }
  }
}
