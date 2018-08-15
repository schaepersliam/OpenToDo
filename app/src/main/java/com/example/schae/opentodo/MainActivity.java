package com.example.schae.opentodo;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.schae.opentodo.data.Contract;
import com.example.schae.opentodo.data.ItemInfo;

import java.util.ArrayList;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class MainActivity extends AppCompatActivity {

    Dialog AddDialog;
    private EntryAdapter adapter;
    public static ArrayList<ItemInfo> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = new ArrayList<>();
        if (mList.size() == 0) {
            refreshList();
        }

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EntryAdapter(mList);

        recyclerView.setHasFixedSize(true);
        adapter.setHasStableIds(true);

        final Button add = findViewById(R.id.todo_add_button);
        final ImageButton remove = findViewById(R.id.remove_button);
        final ImageButton all_done = findViewById(R.id.clear_all_todos);

        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.divideritemdecoration));
        recyclerView.addItemDecoration(itemDecor);

        recyclerView.setItemAnimator(new FadeInAnimator());
        recyclerView.getItemAnimator().setRemoveDuration(400);
        recyclerView.getItemAnimator().setAddDuration(400);
        recyclerView.getItemAnimator().setMoveDuration(400);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialog = new Dialog(MainActivity.this);
                AddDialog.setContentView(R.layout.custom_alertdialog_add_todo);
                AddDialog.setTitle("Add new ToDo");
                final Button add = AddDialog.findViewById(R.id.dialog_add_todo);
                final EditText input_todo = AddDialog.findViewById(R.id.todo_input_edit_text);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!input_todo.getText().toString().isEmpty()) {
                            insertData(input_todo.getText().toString());
                            AddDialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this,"Please fill in a ToDo to continue!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Objects.requireNonNull(AddDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                AddDialog.show();
                adapter.notifyDataSetChanged();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=mList.size()-1; i>=0; i--) {
                    if (mList.get(i).getChecked()) {
                        getContentResolver().delete(Contract.Entry.CONTENT_URI, Contract.Entry._ID + "=" + mList.get(i).getId(),null);
                        mList.get(i).setPrevRemoved(true);
                        mList.get(i).setChecked(false);
                        mList.remove(mList.get(i));
                        adapter.notifyItemRemoved(i);
                    }
                }
                //Just for testing
                Log.i("List size:", " " + mList.size());
            }
        });

        all_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletedRows = getContentResolver().delete(Contract.Entry.CONTENT_URI,null,null);
                getContentResolver().notifyChange(Contract.Entry.CONTENT_URI,null);
                Log.e("Deleted Rows: ","" + deletedRows);
                for (int i=mList.size() -1;i>=0;i--) {
                    mList.get(i).setPrevRemoved(true);
                    mList.get(i).setChecked(false);
                    mList.remove(i);
                    adapter.notifyItemRemoved(i);
                }
                Toast.makeText(MainActivity.this, "All ToDo's removed!", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        ContentValues values = new ContentValues();
        values.put(Contract.Entry.COLUMN_CHECKBOX, Contract.Entry.COLUMN_CHECKBOX_CHECKED);
        for (int i=0;i<mList.size();i++) {
            if (mList.get(i).getChecked()) {
                getContentResolver().update(mList.get(i).getUri(),values, Contract.Entry._ID + "=" + mList.get(i).getId(),null);
            }
        }
        super.onStop();
    }

    public void insertData(String todo) {
        ContentValues values = new ContentValues();
        values.put(Contract.Entry.COLUMN_TODO, todo);
        values.put(Contract.Entry.COLUMN_NOTE, "");
        Uri newRowId = getContentResolver().insert(Contract.Entry.CONTENT_URI,values);


        String[] projections = {Contract.Entry._ID};
        Cursor cursor = getContentResolver().query(Contract.Entry.CONTENT_URI,projections, null,null,null);
        int id = 0;
        int position = 0;
        if (cursor != null) {
            cursor.moveToLast();
            id = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Entry._ID));
            position = cursor.getPosition();
            cursor.close();
        }

        mList.add(new ItemInfo(newRowId,id,todo,"",false,false));
        adapter.notifyItemInserted(position);
    }

    public void refreshList() {
        String[] projections = {Contract.Entry._ID, Contract.Entry.COLUMN_TODO, Contract.Entry.COLUMN_CHECKBOX, Contract.Entry.COLUMN_NOTE};
        Cursor cursor = getContentResolver().query(Contract.Entry.CONTENT_URI,projections,null,null,null);

        int currentId;
        String currentText;
        String currentNote;
        int currentCheckStateInt;
        boolean currentCheckStateBool;
        Uri currentUri;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                currentId = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Entry._ID));
                currentText = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Entry.COLUMN_TODO));
                currentCheckStateInt = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Entry.COLUMN_CHECKBOX));
                currentUri = ContentUris.withAppendedId(Contract.Entry.CONTENT_URI,currentId);
                currentNote = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Entry.COLUMN_NOTE));

                currentCheckStateBool = currentCheckStateInt == 1;

                mList.add(new ItemInfo(currentUri, currentId, currentText, currentNote, currentCheckStateBool,false));
            }
            cursor.close();
        }
        //Just for testing
        Log.i("List size:"," " + mList.size());
    }


}