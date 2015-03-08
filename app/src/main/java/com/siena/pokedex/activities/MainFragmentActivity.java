package com.siena.pokedex.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PokedexApp.getInstance().inject(this);
    Realm realm = Realm.getInstance(this);
    Table pokeTable = realm.getTable(Pokemon.class);

    if (pokeTable.count(1, "bulbasaur") == 0) {
      // TODO do this on a background thread
      PopulateRealm populate = new PopulateRealm(this);
      populate.addEverything();
    }

    Log.i("MainFragmentActivity", realm.getPath());

    setContentView(R.layout.activity_main_fragment);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.container, new PokeListFragment())
          .commit();
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
