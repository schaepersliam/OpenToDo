package com.example.schae.opentodo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter<Entry> {
    public EntryAdapter(Activity context, ArrayList<Entry> Entries) {
        super(context,0, Entries);
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listitem, parent, false);
        }

        final Entry currentItem = getItem(position);

        final TextView text = listItemView.findViewById(R.id.entry_textview);
        text.setText(currentItem.getText());

        final CheckBox cb = listItemView.findViewById(R.id.checkbox);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked()) {
                    currentItem.setIsChecked(true);
                } else {currentItem.setIsChecked(false);}
            }
        });
        listItemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                cb.setChecked(false);
                cb.jumpDrawablesToCurrentState();
            }
        });

        return listItemView;
    }
}
