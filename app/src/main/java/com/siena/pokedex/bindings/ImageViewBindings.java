package com.siena.pokedex.bindings;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.siena.pokedex.PokedexApp;
import com.squareup.picasso.Picasso;

/**
 * Created by Siena Aguayo on 5/28/16.
 */
public class ImageViewBindings {
  @BindingAdapter("android:imageResource")
  public static void setImage(ImageView view, int resourceId) {
    // TODO: should DI Picasso
    Picasso picasso = Picasso.with(PokedexApp.getInstance());
    picasso.load(resourceId).into(view);
  }
}
