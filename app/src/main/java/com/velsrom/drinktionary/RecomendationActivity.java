package com.velsrom.drinktionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RecomendationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendation);

        //Cuando haces busqueda para una recomendacion

        Intent data = getIntent();
        String query = data.getStringExtra("QUERY");
        TextView tv = findViewById(R.id.textView3);
        tv.setText(query);
    }
}