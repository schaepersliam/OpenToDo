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

        if(text.length() > 75) {
            String long_text = text.substring(0,75);
            holder.mTextView.setText(long_text + "...");
        } else {holder.mTextView.setText(text);}
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mCheckBox.isChecked()) {
                    currentItem.setChecked(true);
                } else {currentItem.setChecked(false);}
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
