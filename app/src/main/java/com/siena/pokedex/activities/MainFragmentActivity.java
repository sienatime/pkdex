package com.siena.pokedex.activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.siena.pokedex.DataAdapter;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.PopulateRealm;
import com.siena.pokedex.R;
import com.siena.pokedex.fragments.PokeListFragment;
import com.siena.pokedex.models.Pokemon;
import com.squareup.otto.Bus;
import io.realm.Realm;
import io.realm.internal.Table;
import javax.inject.Inject;

public class MainFragmentActivity extends Activity {
  @Inject Bus bus;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Context context = this;
    PokedexApp.getInstance().inject(this);
    final Realm realm = Realm.getInstance(this);
    Table pokeTable = realm.getTable(Pokemon.class);

    if (pokeTable.count(1, "bulbasaur") == 0) {
      new AsyncTask<Context, Integer, Long>() {
        protected Long doInBackground(Context... aParams) {
          publishProgress(1);
          Realm asyncRealm = Realm.getInstance(context);
          DataAdapter dataAdapter = new DataAdapter(context);
          publishProgress(5);

          asyncRealm.beginTransaction();
          PopulateRealm.addPokemonData(asyncRealm, dataAdapter);
          publishProgress(16);
          PopulateRealm.addTypeData(asyncRealm, dataAdapter);
          publishProgress(32);
          PopulateRealm.addTypeNames(asyncRealm, dataAdapter);
          publishProgress(48);
          PopulateRealm.addSpeciesNames(asyncRealm, dataAdapter);
          publishProgress(60);
          PopulateRealm.addTypeEfficacy(asyncRealm, dataAdapter);
          publishProgress(72);
          PopulateRealm.addLocationAreaProse(asyncRealm, dataAdapter);
          asyncRealm.commitTransaction();
          publishProgress(80);
          asyncRealm.beginTransaction();
          PopulateRealm.addEncounters(asyncRealm, dataAdapter);
          asyncRealm.commitTransaction();
          publishProgress(88);

          asyncRealm.close();
          return 100L;
        }

        protected void onProgressUpdate(Integer... progress) {
          Log.i("PopulateRealm", String.format("%s%%", progress));
        }

        protected void onPostExecute(Long aResult) {
          Log.i("PopulateRealm", "Done!");
          Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
          instantiateListFragment(savedInstanceState);
        }
      }.execute();
    } else {
      instantiateListFragment(savedInstanceState);
    }
    realm.close();
  }

  private void instantiateListFragment(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main_fragment);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction().add(R.id.container, new PokeListFragment()).commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    } else if (id == android.R.id.home) {
      getFragmentManager().popBackStack();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onResume() {
    super.onResume();
    bus.register(this);
  }

  @Override protected void onPause() {
    bus.unregister(this);
    super.onPause();
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    getFragmentManager().popBackStack();
  }
}
