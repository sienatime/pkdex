package com.siena.pokedex.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
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

public class MainFragmentActivity extends ActionBarActivity {
  @Inject Bus bus;
  @InjectView(R.id.toolbar) Toolbar toolbar;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_fragment);
    ButterKnife.inject(this);
    setSupportActionBar(toolbar);
    final Context context = this;
    PokedexApp.getInstance().inject(this);
    Realm realm = Realm.getInstance(this);
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
          publishProgress(10);
          PopulateRealm.addTypeData(asyncRealm, dataAdapter);
          publishProgress(20);
          PopulateRealm.addTypeEfficacy(asyncRealm, dataAdapter);
          publishProgress(30);
          PopulateRealm.addEncounters(asyncRealm, dataAdapter);
          publishProgress(70);
          PopulateRealm.addEncounterConditionValueId(asyncRealm, dataAdapter);
          publishProgress(80);
          PopulateRealm.consolidateAllEncounters(asyncRealm);
          publishProgress(90);
          asyncRealm.commitTransaction();

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
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().add(R.id.container, new PokeListFragment()).commit();
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
      getSupportFragmentManager().popBackStack();
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
