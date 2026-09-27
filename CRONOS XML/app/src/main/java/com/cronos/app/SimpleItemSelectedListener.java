package com.cronos.app;

import android.view.View;
import android.widget.AdapterView;

public abstract class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
    public abstract void onSelected(int position);

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        onSelected(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Sin acción.
    }
}
