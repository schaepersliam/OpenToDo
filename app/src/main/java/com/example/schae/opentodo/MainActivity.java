package com.example.schae.opentodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Entry> entries = new ArrayList<>();
        final EntryAdapter adapter = new EntryAdapter(this, entries);
        final ListView list_view = findViewById(R.id.list_view);
        final EditText input = findViewById(R.id.todo_input_edittext);
        final Button add = findViewById(R.id.todo_add_button);
        final Button remove = findViewById(R.id.remove_button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_text = input.getText().toString();
                if (input_text.isEmpty()) {
                    Toast toast = Toast.makeText(MainActivity.this,"Your ToDo is empty!",Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    entries.add(new Entry(input_text));
                    adapter.notifyDataSetChanged();
                    input.setText("");
                    Log.e("Input: ", input_text);
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=entries.size()-1; i >= 0; i--) {
                    if (entries.get(i).getIsChecked()) {
                        entries.remove(entries.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        list_view.setAdapter(adapter);
    }
}