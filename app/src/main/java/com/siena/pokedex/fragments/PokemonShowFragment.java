package com.siena.pokedex.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.R;
import com.siena.pokedex.adapters.PokemonShowAdapter;
import com.siena.pokedex.models.persisted.Pokemon;
import io.realm.Realm;

import static com.siena.pokedex.ResourceUtil.getStringForIdentifier;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class PokemonShowFragment extends Fragment {
  @BindView(R.id.poke_info_listview) RecyclerView recyclerView;
  private ActionBar actionBar;

  public PokemonShowFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_pokemon_show, container, false);
    ButterKnife.bind(this, rootView);
    actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    int id = getArguments().getInt(PokedexApp.BUNDLE_KEY_POKEMON_ID);

    Realm realm = Realm.getDefaultInstance();
    Pokemon poke = realm.where(Pokemon.class).equalTo("id", id).findFirst();
    actionBar.setTitle(getStringForIdentifier("pokemon_species_name_", poke.getId()));
    recyclerView.setAdapter(new PokemonShowAdapter(poke, rootView.getContext()));
    recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    return rootView;
  }

  @Override public void onDetach() {
    actionBar.setTitle(R.string.app_name);
    super.onDetach();
  }
}
