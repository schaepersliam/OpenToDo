package com.example.schae.opentodo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

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

    private ArrayList<Entry> items;

    EntryAdapter(ArrayList<Entry> entries_input) {
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
        final Entry currentItem = items.get(position);

        holder.itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (currentItem.getPrevDel()) {
                    holder.mCheckBox.setChecked(false);
                    currentItem.setPrevDel(false);
                    holder.mCheckBox.jumpDrawablesToCurrentState();
                } else {
                    if (bottom != oldBottom) {
                        holder.mCheckBox.setChecked(false);
                        holder.mCheckBox.jumpDrawablesToCurrentState();
                    }
                }
            }
        });

        String text = currentItem.getText();

        if(text.length() > 75) {
            String long_text = text.substring(0,75);
            holder.mTextView.setText(long_text + "...");
        } else {holder.mTextView.setText(text);}
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mCheckBox.isChecked()) {
                    currentItem.setIsChecked(true);
                } else {currentItem.setIsChecked(false);}
            }
        });
        setAnimation(holder.itemView,position);
    }


    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            animation.setDuration(750);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
