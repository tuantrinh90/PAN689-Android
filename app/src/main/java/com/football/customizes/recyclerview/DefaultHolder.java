package com.football.customizes.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by toannx
 */

public abstract class DefaultHolder extends RecyclerView.ViewHolder {

    public DefaultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

