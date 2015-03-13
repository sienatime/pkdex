package com.siena.pokedex.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.siena.pokedex.DataAdapter;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.R;
import com.siena.pokedex.models.AllTypeEfficacy;
import com.siena.pokedex.models.Encounter;
import com.siena.pokedex.models.Pokemon;
import com.siena.pokedex.models.PokemonSpeciesName;
import com.siena.pokedex.models.PokemonType;
import com.siena.pokedex.models.TypeName;
import com.squareup.picasso.Picasso;
import io.realm.Realm;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.List;

import static com.siena.pokedex.PokemonUtil.getLocalizedPokeName;
import static com.siena.pokedex.PokemonUtil.getPokemonImageId;
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
  private Context context;
  private Realm realm;

  public PokemonInfoAdapter(Context context, Pokemon pokemon) {
    this.pokemon = pokemon;
    this.context = context;
    this.realm = Realm.getInstance(context);
    setupRows();
  }

  private void setupRows() {
    rows.add(new HeaderRow(HEADER_ROW, pokemon, context));
    rows.add(new SectionHeaderRow(SECTION_HEADER_ROW, R.string.type_effectiveness));

    DataAdapter dataAdapter = new DataAdapter(context);
    AllTypeEfficacy typeEfficacy = dataAdapter.getTypeEfficacy(pokemon.getTypes(), realm);
    //List<Encounter> encounters = dataAdapter.getEncounters(pokemon.getId());

    addTypeEfficacy(typeEfficacy.getWeakTo(), R.string.weak_to);
    addTypeEfficacy(typeEfficacy.getDamagedNormallyBy(), R.string.normal_damage);
    addTypeEfficacy(typeEfficacy.getResistantTo(), R.string.resistant_to);
    addTypeEfficacy(typeEfficacy.getImmuneTo(), R.string.immune_to);

    rows.add(new SectionHeaderRow(SECTION_HEADER_ROW, R.string.locations));

    //addEncounterRows(encounters);
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
    return 5;
  }

  private void addTypeEfficacy(RealmList<PokemonType> types, int stringId) {
    if (types.size() > 0) {
      rows.add(new TypeEfficacyRow(TYPE_EFFICACY_ROW, stringId, context, types));
    }
  }

  private void addEncounterRows(List<Encounter> encounters) {
    if (encounters.size() > 0) {
      for (Encounter encounter : encounters) {
        rows.add(new EncounterRow(context.getResources(), TYPE_ENCOUNTER_ROW, encounter));
      }
    } else {
      rows.add(new NoKnownLocationsRow(TYPE_NO_KNOWN_LOCATIONS_ROW));
    }
  }

  public static class HeaderRow implements Row {
    private Pokemon pokemon;
    private int rowType;
    private Context context;
    private Picasso picasso;

    public HeaderRow(int rowType, Pokemon pokemon, Context context) {
      this.pokemon = pokemon;
      this.rowType = rowType;
      this.context = context;
      this.picasso = Picasso.with(context);
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_poke_header, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      viewHolder.pokeName.setText(getLocalizedPokeName(pokemon));
      Realm realm = Realm.getInstance(context);
      PokemonSpeciesName speciesName = realm.where(PokemonSpeciesName.class)
          .equalTo("pokemonSpeciesId", pokemon.getId())
          .equalTo("localLanguageId", 9)
          .findFirst();
      viewHolder.pokeGenus.setText(
          String.format(context.getString(R.string.genus_format), speciesName.getGenus()));

      int numberOfTypes = pokemon.getTypes().size();

      if (numberOfTypes > 0) {
        setType(viewHolder.type1, pokemon.getTypes().get(0));

        if (numberOfTypes == 2) {
          viewHolder.type2.setVisibility(View.VISIBLE);
          setType(viewHolder.type2, pokemon.getTypes().get(1));
        } else {
          viewHolder.type2.setVisibility(View.GONE);
        }
      }

      int imageId = getPokemonImageId(pokemon);
      if (imageId > 0) {
        viewHolder.pokeImage.setVisibility(View.VISIBLE);
        picasso.load(imageId).into(viewHolder.pokeImage);
      } else {
        Log.e("listadapter", "couldn't find image for id " + Integer.toString(pokemon.getId()));
        viewHolder.pokeImage.setVisibility(View.INVISIBLE);
      }

      return convertView;
    }

    private void setType(TextView textView, PokemonType type) {
      Realm realm = Realm.getInstance(context);
      TypeName typeName = realm.where(TypeName.class)
          .equalTo("typeId", type.getTypeId())
          .equalTo("localLanguageId", 9)
          .findFirst();
      textView.setText(typeName.getName());
      textView.setBackgroundColor(getTypeColor(type.getTypeId()));
    }

    static class ViewHolder {
      @InjectView(R.id.header_poke_name) TextView pokeName;
      @InjectView(R.id.header_poke_genus) TextView pokeGenus;
      @InjectView(R.id.pokemon_type_1) TextView type1;
      @InjectView(R.id.pokemon_type_2) TextView type2;
      @InjectView(R.id.header_poke_image) ImageView pokeImage;

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
      if (convertView == null) {
        convertView = LayoutInflater.from(PokedexApp.getInstance())
            .inflate(R.layout.row_type_efficacy, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.typeEfficacyLevel.setText(titleId);

      SpannableStringBuilder spannableString = new SpannableStringBuilder();

      Realm innerRealm = Realm.getInstance(context);
      for (PokemonType type : types) {
        TypeName typeName = innerRealm.where(TypeName.class)
            .equalTo("typeId", type.getTypeId())
            .equalTo("localLanguageId", 9)
            .findFirst();
        String displayName = " " + typeName.getName().toUpperCase() + " ";
        int color = getTypeColor(type.getTypeId());

        int start = spannableString.length();
        int end = start + displayName.length();
        spannableString.append(displayName);
        spannableString.setSpan(new BackgroundColorSpan(color), start, end, 0);
        spannableString.append(" ");
      }
      innerRealm.close();

      //android:textSize="12sp"
      //android:textStyle="bold"
      //android:textAllCaps="true"
      //android:textColor="@android:color/white"
      //android:shadowColor="@color/shadow_gray"
      //android:shadowDx="0"
      //android:shadowDy="2"
      //android:shadowRadius="4"
      //android:padding="4dp"

      Resources res = PokedexApp.getInstance().getResources();
      viewHolder.typeAnchor.setTextSize(12);
      viewHolder.typeAnchor.setText(spannableString, TextView.BufferType.SPANNABLE);
      viewHolder.typeAnchor.setTextColor(
          PokedexApp.getInstance().getResources().getColor(R.color.white));
      viewHolder.typeAnchor.setShadowLayer(4, 0, 2, res.getColor(R.color.shadow_gray));
      viewHolder.typeAnchor.setTypeface(viewHolder.typeAnchor.getTypeface(), Typeface.BOLD);

      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.type_efficacy_level) TextView typeEfficacyLevel;
      @InjectView(R.id.type_anchor) TextView typeAnchor;

      public ViewHolder(View source) {
        ButterKnife.inject(this, source);
      }
    }
  }

  public static class EncounterRow implements Row {
    private Resources res;
    private int rowType;
    private Encounter encounter;

    public EncounterRow(Resources res, int rowType, Encounter encounter) {
      this.res = res;
      this.rowType = rowType;
      this.encounter = encounter;
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
          String.format(res.getString(R.string.level), encounter.getLevelRange()));
      viewHolder.encounterLocation.setText(encounter.getLocationName());
      viewHolder.encounterMethod.setText(encounter.getMethod());
      viewHolder.encounterRate.setText(String.format(res.getString(R.string.encounter_rate),
          Integer.toString(encounter.getRate())));

      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.encounter_location) TextView encounterLocation;
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
