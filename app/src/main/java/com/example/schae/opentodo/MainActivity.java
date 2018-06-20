package com.example.schae.opentodo;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String input_text = "";
    Dialog AddDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Entry> entries = new ArrayList<>();
        final EntryAdapter adapter = new EntryAdapter(this, entries);
        final ListView list_view = findViewById(R.id.list_view);
        final Button add = findViewById(R.id.todo_add_button);
        final ImageButton remove = findViewById(R.id.remove_button);

        final Animation fadeout = AnimationUtils.loadAnimation(MainActivity.this,android.R.anim.fade_out);
        fadeout.setDuration(500);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialog = new Dialog(MainActivity.this);
                AddDialog.setContentView(R.layout.custom_alertdialog_add_todo);
                AddDialog.setTitle("Add new ToDo");
                Button add = AddDialog.findViewById(R.id.dialog_add_todo);
                final EditText input_todo = AddDialog.findViewById(R.id.todo_input_edittext);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!input_todo.getText().toString().isEmpty()) {
                            entries.add(new Entry(input_todo.getText().toString()));
                            adapter.notifyDataSetChanged();
                            AddDialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this,"Please fill in a ToDo to continue!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                AddDialog.show();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=entries.size()-1; i >= 0; i--) {
                    final int index = i;
                    if (entries.get(index).getIsChecked()) {
                        entries.get(index).setPrevDel(true);
                        list_view.getChildAt(index).startAnimation(fadeout);
                        list_view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                entries.remove(entries.get(index));
                                adapter.notifyDataSetChanged();
                            }
                        },fadeout.getDuration());
                    }
                }
            }
        });

        list_view.setAdapter(adapter);
    }
}