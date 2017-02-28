package com.siena.pokedex.adapters;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by siena on 2/27/17.
 */

class BindingViewHolder extends RecyclerView.ViewHolder {
  ViewDataBinding binding;

  void bind(Object viewModel) {
    if (binding != null) {
      binding.setVariable(BR.viewModel, viewModel);
    }
  }

  View getView() {
    return binding.getRoot();
  }

  BindingViewHolder(ViewDataBinding binding) {
    super(binding.getRoot());
    this.binding = binding;
  }

  BindingViewHolder(View view) {
    super(view);
  }
}
