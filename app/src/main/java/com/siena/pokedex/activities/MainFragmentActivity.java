package com.siena.pokedex.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.R;
import com.siena.pokedex.bus.ShowPokemonInfoEvent;
import com.siena.pokedex.fragments.PokeInfoFragment;
import com.siena.pokedex.fragments.PokeListFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;

public class MainFragmentActivity extends ActionBarActivity {
  @Inject Bus bus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PokedexApp.getInstance().inject(this);
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

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
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

  @Subscribe public void onShowInfoEvent(ShowPokemonInfoEvent event) {
    Fragment fragment = new PokeInfoFragment();
    FragmentTransaction transaction =
        getFragmentManager().beginTransaction().replace(R.id.container, fragment);
    transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
