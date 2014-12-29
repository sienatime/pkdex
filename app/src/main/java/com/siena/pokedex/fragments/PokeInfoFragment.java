package com.siena.pokedex.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.siena.pokedex.R;
import com.siena.pokedex.adapters.PokemonInfoAdapter;
import com.siena.pokedex.models.Pokemon;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class PokeInfoFragment extends Fragment {
  @InjectView(R.id.poke_info_listview) ListView listView;

  public PokeInfoFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_poke_info_listview, container, false);
    ButterKnife.inject(rootView);
    Pokemon poke = new Pokemon(1, "bulbasaur");
    listView.setAdapter(new PokemonInfoAdapter(getActivity(), poke));
    return rootView;
  }
}
