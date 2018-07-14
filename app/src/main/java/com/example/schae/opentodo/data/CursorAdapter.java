package com.example.schae.opentodo.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schae.opentodo.R;

public class CursorAdapter extends android.widget.CursorAdapter {

    public CursorAdapter(Context context, Cursor cursor) {
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listitem,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_todo = (TextView)view.findViewById(R.id.entry_text_view);
        String todo = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Entry.COLUMN_TODO));
        tv_todo.setText(todo);
    }
}
