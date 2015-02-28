package com.siena.pokedex;

import android.content.Context;
import android.database.Cursor;
import com.siena.pokedex.models.Pokemon;
import com.siena.pokedex.models.PokemonSpeciesName;
import com.siena.pokedex.models.PokemonType;
import com.siena.pokedex.models.TypeName;
import io.realm.Realm;

/**
 * Created by Siena Aguayo on 2/28/15.
 */
public class PopulateRealm {
  private Context context;
  private Realm realm;
  private DataAdapter mDbHelper;

  public PopulateRealm(Context context) {
    this.context = context;
    mDbHelper = new DataAdapter(context);
    mDbHelper.createDatabase();
    realm = Realm.getInstance(context);
  }

  public void addEverything() {
    addPokemonData();
    addTypeData();
    addTypeNames();
    addSpeciesNames();
  }

  public void addSpeciesNames() {
    mDbHelper.open();
    Cursor cursor = mDbHelper.getData("SELECT pokemon_species_id, local_language_id, name, genus FROM pokemon_species_names");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++ ) {
      realm.beginTransaction();
      PokemonSpeciesName pokemonSpeciesName = realm.createObject(PokemonSpeciesName.class);
      pokemonSpeciesName.setPokemonSpeciesId(cursor.getInt(0));
      pokemonSpeciesName.setLocalLanguageId(cursor.getInt(1));
      pokemonSpeciesName.setName(cursor.getString(2) != null ? cursor.getString(2) : "");
      String genus = cursor.getString(3);
      if (genus != null) {
        pokemonSpeciesName.setGenus(genus);
      } else {
        pokemonSpeciesName.setGenus("");
      }
      realm.commitTransaction();
      cursor.moveToNext();
    }
    cursor.close();
    mDbHelper.close();
  }

  public void addTypeNames() {
    mDbHelper.open();
    Cursor cursor = mDbHelper.getData("SELECT type_id, local_language_id, name FROM type_names");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++ ) {
      realm.beginTransaction();
      TypeName typeName = realm.createObject(TypeName.class);
      typeName.setTypeId(cursor.getInt(0));
      typeName.setLocalLanguageId(cursor.getInt(1));
      typeName.setName(cursor.getString(2));
      realm.commitTransaction();
      cursor.moveToNext();
    }
    cursor.close();
    mDbHelper.close();
  }

  public void addTypeData() {
    mDbHelper.open();
    Cursor cursor = mDbHelper.getData("SELECT pokemon_id, type_id, slot FROM pokemon_types");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++ ) {
      realm.beginTransaction();
      PokemonType type = realm.createObject(PokemonType.class);
      type.setPokemonId(cursor.getInt(0));
      type.setTypeId(cursor.getInt(1));
      type.setSlot(cursor.getInt(2));
      realm.commitTransaction();
      cursor.moveToNext();
    }
    cursor.close();
    mDbHelper.close();
  }

  public void addPokemonData() {
    mDbHelper.open();

    final Cursor testdata = mDbHelper.getAllPokemonData();
    for (int i = 0; i < 719; i++) {
      realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
          Pokemon poke = realm.createObject(Pokemon.class);
          //_id|identifier|species_id|height|weight|base_experience|order|is_default
          poke.setId(testdata.getInt(0));
          poke.setIdentifier(testdata.getString(1));
          poke.setSpeciesId(testdata.getInt(2));
          poke.setHeight(testdata.getInt(3));
          poke.setWeight(testdata.getInt(4));
          poke.setBaseExperience(testdata.getInt(5));
          poke.setOrder(testdata.getInt(6));
          poke.setDefault(testdata.getInt(7) == 1);
        }
      });

      testdata.moveToNext();
    }
    mDbHelper.close();
  }
}
