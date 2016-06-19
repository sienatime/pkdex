package com.siena.pokedex.viewModels.show;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import com.siena.pokedex.R;
import com.siena.pokedex.models.persisted.PokemonType;
import io.realm.RealmList;

import static com.siena.pokedex.PokemonUtil.getStringForIdentifier;
import static com.siena.pokedex.PokemonUtil.getTypeColor;

/**
 * Created by Siena Aguayo on 6/19/16.
 */
public class TypeEfficacyViewModel {
  public int efficacyLevel;

  public TypeEfficacyViewModel(View view, int titleId, RealmList<PokemonType> types) {
    this.efficacyLevel = titleId;
    GridLayout typeAnchor = (GridLayout) view.findViewById(R.id.type_anchor);
    LayoutInflater inflater = LayoutInflater.from(view.getContext());

    if (typeAnchor.getChildCount() == 0) {
      for (int i = 0; i < types.size(); i++) {
        PokemonType type = types.get(i);
        inflater.inflate(R.layout.textview_type, typeAnchor);
        TextView textView = (TextView) typeAnchor.getChildAt(i);
        textView.setBackgroundColor(getTypeColor(type.getTypeId()));
        textView.setText(getStringForIdentifier(type.getTypeId(), "type_").toUpperCase());
      }
    }
  }
}
