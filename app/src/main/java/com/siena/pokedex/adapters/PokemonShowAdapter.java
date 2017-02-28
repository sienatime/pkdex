package com.siena.pokedex.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.android.databinding.library.baseAdapters.BR;
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
public class PokemonShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<RecyclerRow> rows = new ArrayList<>();
  private Pokemon pokemon;
  private Context context;
  private final int HEADER_ROW = 0;
  private final int SECTION_HEADER_ROW = 1;
  private final int TYPE_EFFICACY_ROW = 2;
  private final int TYPE_ENCOUNTER_ROW = 3;
  private final int TYPE_NO_KNOWN_LOCATIONS_ROW = 4;
  private final int TYPE_VERSION_ROW = 5;

  public PokemonShowAdapter(Pokemon pokemon, Context context) {
    this.pokemon = pokemon;
    this.context = context;
    setupRows();
  }

  private void setupRows() {
    rows.add(new HeaderRow());
    rows.add(new SectionHeaderRow(R.string.type_effectiveness));

    AllTypeEfficacy typeEfficacy = AllTypeEfficacy.createAllTypeEfficacy(pokemon.getTypes());

    addTypeEfficacy(typeEfficacy.getWeakTo(), R.string.weak_to);
    addTypeEfficacy(typeEfficacy.getDamagedNormallyBy(), R.string.normal_damage);
    addTypeEfficacy(typeEfficacy.getResistantTo(), R.string.resistant_to);
    addTypeEfficacy(typeEfficacy.getImmuneTo(), R.string.immune_to);

    rows.add(new SectionHeaderRow(R.string.locations));

    addEncounterRows(pokemon.getConsolidatedEncounters());
  }

  @Override public int getItemViewType(int position) {
    return rows.get(position).getType();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    int layout = layoutIdForRowType(viewType);
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, layout, parent, false);

    if (viewType == TYPE_NO_KNOWN_LOCATIONS_ROW) {
      return new BindingViewHolder(layoutInflater.inflate(layout, parent, false));
    } else {
      return new BindingViewHolder(binding);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    RecyclerRow row = rows.get(position);
    BindingViewHolder BindingViewHolder = (BindingViewHolder) holder;

    if (row.getType() == TYPE_EFFICACY_ROW) {
      TypeEfficacyRow typeEfficacyRow = (TypeEfficacyRow) row;
      BindingViewHolder.bind(new TypeEfficacyViewModel(BindingViewHolder.getView(), typeEfficacyRow.titleId,
          typeEfficacyRow.types));
    } else {
      BindingViewHolder.bind(row.getViewModel());
    }
  }

  @Override public int getItemCount() {
    return rows.size();
  }

  private int layoutIdForRowType(int viewType) {
    switch (viewType) {
      case HEADER_ROW:
        return R.layout.row_show_header;
      case SECTION_HEADER_ROW:
        return R.layout.row_show_section_header;
      case TYPE_EFFICACY_ROW:
        return R.layout.row_show_type_efficacy;
      case TYPE_ENCOUNTER_ROW:
        return R.layout.row_show_encounter;
      case TYPE_NO_KNOWN_LOCATIONS_ROW:
        return R.layout.row_show_no_known_locations;
      case TYPE_VERSION_ROW:
        return R.layout.row_show_version_header;
      default:
        return -1;
    }
  }

  private void addTypeEfficacy(RealmList<PokemonType> types, int stringId) {
    if (types.size() > 0) {
      rows.add(new TypeEfficacyRow(stringId, types));
    }
  }

  private void addEncounterRows(RealmList<ConsolidatedEncounter> encounters) {
    if (encounters.size() > 0) {
      RealmResults<Version> versions = pokemon.getVersions().where().findAllSorted("id");

      for (Version version : versions) {
        Integer versionId = version.id;
        rows.add(new VersionHeaderRow(versionId));
        RealmResults<ConsolidatedEncounter> encountersByVersion =
            encounters.where().equalTo("versionId", versionId).findAll();
        for (ConsolidatedEncounter encounter : encountersByVersion) {
          rows.add(new EncounterRow(encounter));
        }
      }
    } else {
      rows.add(new NoKnownLocationsRow());
    }
  }

  private class HeaderRow implements RecyclerRow {
    HeaderRow() {
    }

    @Override public int getType() {
      return HEADER_ROW;
    }

    @Override public Object getViewModel() {
      return new HeaderViewModel(pokemon, context);
    }
  }

  private class VersionHeaderRow implements RecyclerRow {
    private Integer versionId;

    VersionHeaderRow(Integer versionId) {
      this.versionId = versionId;
    }

    @Override public int getType() {
      return TYPE_VERSION_ROW;
    }

    @Override public Object getViewModel() {
      return new VersionHeaderViewModel(versionId);
    }
  }

  private class TypeEfficacyRow implements RecyclerRow {
    private int titleId;
    private RealmList<PokemonType> types;

    TypeEfficacyRow(int titleId, RealmList<PokemonType> types) {
      this.titleId = titleId;
      this.types = types;
    }

    @Override public int getType() {
      return TYPE_EFFICACY_ROW;
    }

    @Override public Object getViewModel() {
      return null;
    }
  }

  private class SectionHeaderRow implements RecyclerRow {
    private int titleId;

    SectionHeaderRow(int titleId) {
      this.titleId = titleId;
    }

    @Override public int getType() {
      return SECTION_HEADER_ROW;
    }

    @Override public Object getViewModel() {
      return new SectionHeaderViewModel(titleId);
    }
  }

  private class EncounterRow implements RecyclerRow {
    private ConsolidatedEncounter encounter;

    EncounterRow(ConsolidatedEncounter encounter) {
      this.encounter = encounter;
    }

    @Override public int getType() {
      return TYPE_ENCOUNTER_ROW;
    }

    @Override public Object getViewModel() {
      return new EncounterViewModel(encounter);
    }
  }

  private class NoKnownLocationsRow implements RecyclerRow {
    NoKnownLocationsRow() {
    }

    @Override public int getType() {
      return TYPE_NO_KNOWN_LOCATIONS_ROW;
    }

    @Override public Object getViewModel() {
      return null;
    }
  }
}
