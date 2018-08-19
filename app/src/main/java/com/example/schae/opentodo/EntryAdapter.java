package com.example.schae.opentodo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Paint;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
        TextView mPriorityTextView;
        public CheckBox mCheckBox;
        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.entry_text_view);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            mPriorityTextView = itemView.findViewById(R.id.priority_text_view);
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
        Log.e(LOG_TAG,"Position: " + position + "; text: " + currentItem.getText());
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

        if (currentItem.getPriorityState() == 1) {
            holder.mPriorityTextView.setText("!");
        } else if (currentItem.getPriorityState() == 2) {
            holder.mPriorityTextView.setText("!!");
        } else if (currentItem.getPriorityState() == 3) {
            holder.mPriorityTextView.setText("!!!");
        } else {
            holder.mPriorityTextView.setText("");
        }

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
                AddDialog = new Dialog(holder.itemView.getContext());
                AddDialog.setContentView(R.layout.custom_alertdialog_edit_todo);
                AddDialog.setTitle("Change ToDo");
                final Button change_button = AddDialog.findViewById(R.id.dialog_add_todo);
                final EditText editText = AddDialog.findViewById(R.id.todo_input_edit_text);
                final EditText noteEditText = AddDialog.findViewById(R.id.todo_edit_note);
                final RadioButton priority_high = AddDialog.findViewById(R.id.change_radiobutton_1);
                final RadioButton priority_mid = AddDialog.findViewById(R.id.change_radiobutton_2);
                final RadioButton priority_low = AddDialog.findViewById(R.id.change_radiobutton_3);
                final RadioButton priority_none = AddDialog.findViewById(R.id.change_radiobutton_4);
                priority_none.setPaintFlags(priority_none.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                final RadioGroup priority_group = AddDialog.findViewById(R.id.change_radio_group);
                change_button.setText("Change");
                editText.setText(currentItem.getText());
                editText.setSelection(currentItem.getText().length());
                noteEditText.setText(currentItem.getNote());
                if (currentItem.getPriorityState() == 1) {
                    priority_low.setChecked(true);
                } else if (currentItem.getPriorityState() == 2) {
                    priority_mid.setChecked(true);
                } else if (currentItem.getPriorityState() == 3) {
                    priority_high.setChecked(true);
                }
                change_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int PriorityState = 0;
                        ContentValues values = new ContentValues();
                        if (priority_group.getCheckedRadioButtonId() == priority_high.getId()) {
                            values.put(Contract.Entry.COLUMN_PRIORITY_STATE,3);
                            PriorityState = 3;
                        } else if (priority_group.getCheckedRadioButtonId() == priority_mid.getId()) {
                            values.put(Contract.Entry.COLUMN_PRIORITY_STATE,2);
                            PriorityState = 2;
                        } else if (priority_group.getCheckedRadioButtonId() == priority_low.getId()) {
                            values.put(Contract.Entry.COLUMN_PRIORITY_STATE,1);
                            PriorityState = 1;
                        } else if (priority_group.getCheckedRadioButtonId() == priority_none.getId()) {
                            values.put(Contract.Entry.COLUMN_PRIORITY_STATE,0);
                            PriorityState = 0;
                        }
                        items.get(holder.getAdapterPosition()).setPriorityState(PriorityState);
                        if (!currentItem.getText().equals(editText.getText().toString())) {
                            values.put(Contract.Entry.COLUMN_TODO,editText.getText().toString());
                            items.get(holder.getAdapterPosition()).setText(editText.getText().toString());
                        }
                        if (!currentItem.getNote().equals(noteEditText.getText().toString())) {
                            values.put(Contract.Entry.COLUMN_NOTE,noteEditText.getText().toString());
                            items.get(holder.getAdapterPosition()).setNote(noteEditText.getText().toString());
                        }
                        Log.e(LOG_TAG,"Current values: " + values);
                        int updatedRow = holder.itemView.getContext().getContentResolver().update(currentItem.getUri(),values,null,null);
                        if (updatedRow == 0) {
                            Log.e(LOG_TAG,"Updating the item failed!");
                        } else {
                            Log.e(LOG_TAG,"Updating the item succeeded!");
                        }
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
