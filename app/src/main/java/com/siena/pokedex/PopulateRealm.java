package com.siena.pokedex;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;
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
  private DataAdapter dataAdapter;

  public PopulateRealm(Context context) {
    this.context = context;
    this.dataAdapter = new DataAdapter(context);
    this.realm = Realm.getInstance(context);
  }

  //protected Long doInBackground(Context... contexts) {
  //  Realm = null;
  //  try {
  //    realm = Realm.getInstance(contexts[0]);
  //
  //    // ... Use the Realm instance
  //  } finally {
  //    if (realm != null) {
  //      realm.close();
  //    }
  //  }
  //}

  public void addEverything() {
    new AsyncTask<Context, Integer, Long>() {
      protected Long doInBackground(Context... aParams) {
        // do some expensive work
        // in the background here
        addPokemonData();
        addTypeNames();
        addSpeciesNames();
        return 100L;
      }

      protected void onProgressUpdate(Integer... progress) {
      }

      protected void onPostExecute(Long aResult) {
        // background work is finished,
        // we can update the UI here
        // including removing the dialog
        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
      }
    }.execute();
  }

  public void addSpeciesNames() {
    Cursor cursor = dataAdapter.getData(
        "SELECT pokemon_species_id, local_language_id, name, genus FROM pokemon_species_names");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++) {
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
  }

  public void addTypeNames() {
    Cursor cursor = dataAdapter.getData("SELECT type_id, local_language_id, name FROM type_names");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++) {
      realm.beginTransaction();
      TypeName typeName = realm.createObject(TypeName.class);
      typeName.setTypeId(cursor.getInt(0));
      typeName.setLocalLanguageId(cursor.getInt(1));
      typeName.setName(cursor.getString(2));
      realm.commitTransaction();
      cursor.moveToNext();
    }
    cursor.close();
  }

  public void addTypeData(Pokemon pokemon) {
    Cursor cursor = dataAdapter.getData(
        "SELECT pokemon_id, type_id, slot FROM pokemon_types WHERE pokemon_id = "
            + pokemon.getId());
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++) {
      realm.beginTransaction();
      PokemonType type = realm.createObject(PokemonType.class);
      type.setPokemonId(cursor.getInt(0));
      type.setTypeId(cursor.getInt(1));
      type.setSlot(cursor.getInt(2));
      pokemon.getTypes().add(type);
      realm.commitTransaction();
      cursor.moveToNext();
    }
    cursor.close();
  }

  public void addPokemonData() {
    final Cursor testdata = dataAdapter.getAllPokemonData();
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

          addTypeData(poke);
        }
      });

      testdata.moveToNext();
    }
  }
}
