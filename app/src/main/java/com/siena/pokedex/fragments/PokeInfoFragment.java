package com.siena.pokedex.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.siena.pokedex.DataAdapter;
import com.siena.pokedex.PokemonUtil;
import com.siena.pokedex.R;
import com.siena.pokedex.adapters.PokemonInfoAdapter;
import com.siena.pokedex.models.Pokemon;
import io.realm.Realm;
import java.util.List;

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
    ButterKnife.inject(this, rootView);
    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    int id = getArguments().getInt(PokemonUtil.POKEMON_ID_KEY);

    DataAdapter mDbHelper = new DataAdapter(getActivity());
    mDbHelper.createDatabase();
    mDbHelper.open();
    Realm realm = Realm.getInstance(getActivity());

    String genus = mDbHelper.getGenusById(id);
    List<Integer> typeIds = mDbHelper.getPokemonTypeData(id);
    //ArrayList<Pokemon.Type> types = new ArrayList<>();
    //for (Integer typeId : typeIds) {
    //  Pokemon.Type type = new Pokemon.Type(typeId, mDbHelper.getTypeById(typeId));
    //  types.add(type);
    //}

    Pokemon poke = realm.where(Pokemon.class).equalTo("id", id).findFirst();
    //poke.setTypes(types);
    //poke.setGenus(genus);
    listView.setAdapter(new PokemonInfoAdapter(getActivity(), poke));
    return rootView;
  }
}
