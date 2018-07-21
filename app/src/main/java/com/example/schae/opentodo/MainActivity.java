package com.example.schae.opentodo;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.schae.opentodo.data.Contract;
import com.example.schae.opentodo.data.ItemInfo;
import com.example.schae.opentodo.data.SQLiteHelper;
import com.example.schae.opentodo.data.ToDoCursorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Dialog AddDialog;
    private ToDoCursorAdapter adapter;
    int LOADER = 1;
    public static ArrayList<ItemInfo> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = new ArrayList<>();
        if (mList.size() == 0) {
            refreshList();
        }

        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToDoCursorAdapter(this,null);

        final Button add = findViewById(R.id.todo_add_button);
        final ImageButton remove = findViewById(R.id.remove_button);
        final ImageButton all_done = findViewById(R.id.clear_all_todos);

        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.divideritemdecoration));
        recyclerView.addItemDecoration(itemDecor);

        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);

        //@TODO Add new animations for insert and deletion and slide up

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
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletedRows = 0;
                int updatedRow;
                ContentValues values = new ContentValues();
                for (int i=0;i<mList.size();i++) {
                    if (mList.get(i).getChecked()) {
                        Log.e("Test","Succeeded!");
                        values.put(Contract.Entry.COLUMN_CHECKBOX, Contract.Entry.COLUMN_CHECKBOX_CHECKED);
                        deletedRows = getContentResolver().delete(Contract.Entry.CONTENT_URI, Contract.Entry._ID + "=" + mList.get(i).getId(),null);
                        adapter.notifyItemRemoved(deletedRows);
                        mList.get(i).setRemoved(true);
                    }
                }
                for (int i=mList.size() -1;i>=0;i--) {
                    if (mList.get(i).getRemoved()) {
                        mList.remove(i);
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
                Log.e("Deleted Rows: ","" + deletedRows);
                adapter.notifyItemRemoved(deletedRows);
                mList.clear();
                Toast.makeText(MainActivity.this, "All ToDo's removed!", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);
        getLoaderManager().initLoader(LOADER,null,this);
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
        Uri newRowId = getContentResolver().insert(Contract.Entry.CONTENT_URI,values);
        getContentResolver().notifyChange(newRowId,null);

        String[] projections = {Contract.Entry._ID};
        Cursor cursor = getContentResolver().query(Contract.Entry.CONTENT_URI,projections, null,null,null);
        cursor.moveToLast();
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Entry._ID));
        cursor.close();
        mList.add(new ItemInfo(newRowId,id,todo,false,false));
    }

    public void refreshList() {
        String[] projections = {Contract.Entry._ID, Contract.Entry.COLUMN_TODO, Contract.Entry.COLUMN_CHECKBOX};
        Cursor cursor = getContentResolver().query(Contract.Entry.CONTENT_URI,projections,null,null,null);

        int currentId = 0;
        String currentText = null;
        int currentCheckStateInt = 0;
        boolean currentCheckStateBool;
        Uri currentUri = null;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                currentId = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Entry._ID));
                currentText = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Entry.COLUMN_TODO));
                currentCheckStateInt = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Entry.COLUMN_CHECKBOX));
                currentUri = ContentUris.withAppendedId(Contract.Entry.CONTENT_URI,currentId);

                if (currentCheckStateInt == 1) {
                    currentCheckStateBool = true;
                } else {currentCheckStateBool = false;}

                mList.add(new ItemInfo(currentUri,currentId,currentText,currentCheckStateBool,false));
            }
            cursor.close();
        }
        //Just for testing
        Log.i("List size:"," " + mList.size());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,Contract.Entry.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}