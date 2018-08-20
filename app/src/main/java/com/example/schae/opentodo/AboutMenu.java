package com.example.schae.opentodo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_menu);

        TextView tv_1 = findViewById(R.id.about_tv_1);
        TextView tv_2 = findViewById(R.id.about_tv_2);
        TextView tv_3 = findViewById(R.id.about_tv_3);
        TextView tv_4 = findViewById(R.id.about_tv_4);

        Button close_button = findViewById(R.id.close_button_about_menu);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_1.setText(R.string.about_1);
        tv_1.append(new String(Character.toChars(0x1F49B)));

        tv_2.setText(R.string.about_2);

        tv_3.setText(R.string.about_3);
        tv_3.append(new String(Character.toChars(0x1F4CD)));

        tv_4.setText(R.string.about_license);
        tv_4.append(new String(Character.toChars(0x2757)));
    }
}
