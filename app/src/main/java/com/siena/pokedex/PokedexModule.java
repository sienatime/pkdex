package com.siena.pokedex;

/**
 * Created by Siena Aguayo on 12/28/14.
 */

import com.siena.pokedex.activities.MainFragmentActivity;
import com.siena.pokedex.adapters.PokemonRecyclerViewAdapter;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = { PokedexApp.class, MainFragmentActivity.class, PokemonRecyclerViewAdapter.class }
)
public class PokedexModule {
  @Provides @Singleton Bus provideBus() {
    return new Bus();
  }
}
