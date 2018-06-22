package com.example.schae.opentodo;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Entry> entries = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.divideritemdecoration));
        recyclerView.addItemDecoration(itemDecor);

        adapter = new EntryAdapter(entries);

        final Button add = findViewById(R.id.todo_add_button);
        final ImageButton remove = findViewById(R.id.remove_button);

        final Animation fadeout = AnimationUtils.loadAnimation(MainActivity.this,android.R.anim.fade_out);
        fadeout.setDuration(500);

        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);

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
                            AddDialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this,"Please fill in a ToDo to continue!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                AddDialog.show();
                adapter.notifyDataSetChanged();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=entries.size()-1; i >= 0; i--) {
                    final int index = i;
                    if (entries.get(index).getIsChecked()) {
                        entries.get(index).setPrevDel(true);
                        entries.remove(entries.get(index));
                        adapter.notifyItemRemoved(index);
                    }
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }
}