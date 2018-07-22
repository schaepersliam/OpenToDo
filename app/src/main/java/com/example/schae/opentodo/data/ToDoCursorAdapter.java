package com.example.schae.opentodo.data;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.schae.opentodo.MainActivity;
import com.example.schae.opentodo.R;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.net.URI;
import java.util.EmptyStackException;

public class ToDoCursorAdapter extends RecyclerView.Adapter<ToDoCursorAdapter.CursorViewHolder> {

    private Context mContext;
    private static Cursor mCursor;
    private int lastPosition = -1;

    public ToDoCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    public class CursorViewHolder extends RecyclerView.ViewHolder {

        public TextView todoText;
        public CheckBox checkBox;

        public CursorViewHolder(View itemView) {
            super(itemView);

            todoText = itemView.findViewById(R.id.entry_text_view);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    @NonNull
    @Override
    public CursorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.listitem,parent,false);
        return new CursorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CursorViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        final String todo_text = mCursor.getString(mCursor.getColumnIndexOrThrow(Contract.Entry.COLUMN_TODO));
        final int is_checkbox_checked = mCursor.getInt(mCursor.getColumnIndexOrThrow(Contract.Entry.COLUMN_CHECKBOX));

        final int id = mCursor.getInt(mCursor.getColumnIndexOrThrow(Contract.Entry._ID));
        final Uri currentUri = ContentUris.withAppendedId(Contract.Entry.CONTENT_URI,id);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i=0;i<MainActivity.mList.size();i++) {
                        if (MainActivity.mList.get(i).getId() == id) {
                            MainActivity.mList.get(i).setChecked(true);
                        }
                    }
                } else {
                    for (int i=0;i<MainActivity.mList.size();i++) {
                        if (MainActivity.mList.get(i).getId() == id) {
                            MainActivity.mList.get(i).setChecked(false);
                        }
                    }
                }
            }
        });

        holder.todoText.setText(todo_text);
        if (is_checkbox_checked == Contract.Entry.COLUMN_CHECKBOX_CHECKED) {
            holder.checkBox.setChecked(true);
        } else if (is_checkbox_checked == Contract.Entry.COLUMN_CHECKBOX_UNCHECKED) {
            for (int i=0;i<MainActivity.mList.size();i++) {
                if (MainActivity.mList.get(i).getId() == id) {
                    if (!MainActivity.mList.get(i).getChecked()) {
                        Log.e("OMG","OMG!!!" + position);
                        holder.checkBox.setChecked(false);
                    }
                }
            }
        }
        setAnimation(holder.itemView,position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            animation.setDuration(750);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }


    public class updateCheckbox extends Thread {

        int id;
        Uri currentUri;
        CursorViewHolder holder;

        updateCheckbox(Uri uri,int id,CursorViewHolder holder) {
            this.id = id;
            currentUri = uri;
            this.holder = holder;
        }

        @Override
        public void run() {
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    holder.checkBox.setChecked(false);
                    holder.checkBox.jumpDrawablesToCurrentState();
                    ContentValues values = new ContentValues();
                    if (isChecked) {
                        holder.checkBox.setChecked(true);
                        holder.checkBox.animate();
                        values.put(Contract.Entry.COLUMN_CHECKBOX, Contract.Entry.COLUMN_CHECKBOX_CHECKED);
                    } else {
                        values.put(Contract.Entry.COLUMN_CHECKBOX, Contract.Entry.COLUMN_CHECKBOX_UNCHECKED);
                    }
                    mContext.getContentResolver().update(currentUri,values, Contract.Entry._ID + "=" + id,null);
                }
            });
        }
    }
}
