package com.siena.pokedex.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.siena.pokedex.R;
import com.siena.pokedex.adapters.PokemonListAdapter;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokeListFragment extends Fragment {
  public PokeListFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_pokemon_listview, container, false);
    ((ListView) rootView.findViewById(R.id.poke_listview)).setAdapter(
        new PokemonListAdapter(getActivity()));
    return rootView;
  }
}
