package com.siena.pokedex.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.siena.pokedex.R;

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
    return rootView;
  }

}
