package com.siena.pokedex;

import android.content.Context;
import android.database.Cursor;
import com.siena.pokedex.models.Pokemon;
import com.siena.pokedex.models.PokemonType;
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
