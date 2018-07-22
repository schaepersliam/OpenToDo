package com.example.schae.opentodo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.schae.opentodo.data.ItemInfo;

import java.util.ArrayList;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {

    private int lastPosition = -1;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        public CheckBox mCheckBox;
        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.entry_text_view);
            mCheckBox = itemView.findViewById(R.id.checkbox);
        }
    }

    private ArrayList<ItemInfo> items;

    EntryAdapter(ArrayList<ItemInfo> entries_input) {
        items = entries_input;
    }

    @NonNull
    @Override
    public EntryAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final EntryAdapter.ViewHolder holder, int position) {
        final ItemInfo currentItem = items.get(position);

        String text = currentItem.getText();

        holder.mTextView.setText(text);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentItem.setChecked(true);
                } else {
                    currentItem.setChecked(false);
                }
            }
        });

        holder.itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (currentItem.getPrevRemoved()) {
                    holder.mCheckBox.setChecked(false);
                    currentItem.setPrevRemoved(false);
                    holder.mCheckBox.jumpDrawablesToCurrentState();
                } else {
                    if (bottom != oldBottom) {
                        if (currentItem.getChecked()) {
                            holder.mCheckBox.setChecked(true);
                            holder.mCheckBox.jumpDrawablesToCurrentState();
                        } else {
                            holder.mCheckBox.setChecked(false);
                            holder.mCheckBox.jumpDrawablesToCurrentState();
                        }

                    }
                }
            }
        });
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position)
    {
        return items.get(position).getId();
    }
}
