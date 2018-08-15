package com.example.schae.opentodo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.schae.opentodo.data.Contract;
import com.example.schae.opentodo.data.ItemInfo;

import java.util.ArrayList;
import java.util.Objects;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {

    private String LOG_TAG = EntryAdapter.class.getSimpleName();

    private Dialog AddDialog;

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
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final EntryAdapter.ViewHolder holder, final int position) {
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e(LOG_TAG,"A clicked has been noticed!");
                AddDialog = new Dialog(holder.itemView.getContext());
                AddDialog.setContentView(R.layout.custom_alertdialog_edit_todo);
                AddDialog.setTitle("Change ToDo");
                Button change_button = AddDialog.findViewById(R.id.dialog_add_todo);
                final EditText editText = AddDialog.findViewById(R.id.todo_input_edit_text);
                final EditText noteEditText = AddDialog.findViewById(R.id.todo_edit_note);
                change_button.setText("Change");
                editText.setText(currentItem.getText());
                noteEditText.setText(currentItem.getNote());
                editText.setSelection(currentItem.getText().length());
                change_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.put(Contract.Entry.COLUMN_TODO,editText.getText().toString());
                        values.put(Contract.Entry.COLUMN_NOTE,noteEditText.getText().toString());
                        int updatedRow = holder.itemView.getContext().getContentResolver().update(currentItem.getUri(),values,null,null);
                        if (updatedRow == 0) {
                            Log.e(LOG_TAG,"Updating the item failed!");
                        } else {
                            Log.e(LOG_TAG,"Updating the item succeeded!");
                        }
                        items.get(holder.getAdapterPosition()).setText(editText.getText().toString());
                        items.get(holder.getAdapterPosition()).setNote(noteEditText.getText().toString());
                        EntryAdapter.this.notifyItemChanged(holder.getAdapterPosition(), "payload " + holder.getAdapterPosition());
                        AddDialog.dismiss();
                    }
                });

                Objects.requireNonNull(AddDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                AddDialog.show();
                return true;
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
