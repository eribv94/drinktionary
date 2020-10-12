package com.velsrom.drinktionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;

    LinearLayout buttonsLayout;
    Button drinktionaryButton, searchButton, newDrinkButton;
    TextView titleTextView;
    SearchView searchDrinkSearchView;
    boolean isSearching = false;
    Button[] buttonsInLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        buttonsLayout = findViewById(R.id.optionButtonsLayout);
        drinktionaryButton = findViewById(R.id.drinktionaryButton);
        searchButton = findViewById(R.id.searchDrinkButton);
        newDrinkButton = findViewById(R.id.newDrinkButton);
        titleTextView= findViewById(R.id.titleTextView);
        searchDrinkSearchView =  findViewById(R.id.searchDrinkSearchView);

        searchDrinkSearchView.animate().alpha(0);
        buttonsInLayout = new Button[]{drinktionaryButton, searchButton, newDrinkButton};

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.velsrom.drinktionary", Context.MODE_PRIVATE);
        Boolean dataCreated = sharedPreferences.getBoolean("createData", true);
        if(dataCreated){
            sharedPreferences.edit().putBoolean("createData", false).apply();
            db.createData();
        }

        searchDrinkSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Obtiene que buscaste en el searchview
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                Intent recomendationIntent = new Intent(getApplicationContext(), RecomendationActivity.class);
                recomendationIntent.putExtra("QUERY", query);
                startActivity(recomendationIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    public void newDrink(View view){
        Intent addDrinkIntent = new Intent(getApplicationContext(), AddDrinkActivity.class);
        startActivity(addDrinkIntent);
    }

    public void goDrinktionary(View view){
        Intent drinktionaryIntent = new Intent(getApplicationContext(), DrinktionaryActivity.class);
        startActivity(drinktionaryIntent);
    }

    public void searchDrink(View view){
        isSearching = !isSearching;
        searchDrinkSearchView.setIconified(false);  //Para que salga teclado y puedas escribir al
                                                    // momento de presionar el boton buscar
        searchAnimation();
    }

    private void searchAnimation(){
        //Ver si mejor poner variables globales y hacer operaciones para revertir animaciones.
        // Ej. yTransition *= -1 o tener 1 alfa y usar negativos para los contrarios. Ej. alfa & alfa * -1
        int fadeDuration = 500;
        int yTransition = 1800;
        boolean enableButtons = true;
        int alfaSearch, alfaButtons;
        if(isSearching) {
            alfaSearch = 1;
            alfaButtons = 0;
            yTransition *= -1;
            enableButtons = false;
        }
        else {
            alfaSearch = 0;
            alfaButtons = 1;
        }
        searchDrinkSearchView.animate().alpha(alfaSearch).translationYBy(yTransition).setDuration(fadeDuration);
        drinktionaryButton.animate().alpha(alfaButtons).setDuration(fadeDuration);
        searchButton.animate().alpha(alfaButtons).setDuration(fadeDuration);
        newDrinkButton.animate().alpha(alfaButtons).setDuration(fadeDuration);
        titleTextView.animate().alpha(alfaButtons).setDuration(fadeDuration);
        for (int i = 0; i < buttonsInLayout.length; i++){
            buttonsInLayout[i].setEnabled(enableButtons);
        }
    }

    @Override
    public void onBackPressed() {
        if(isSearching){
            isSearching = !isSearching;
            searchAnimation();
        }
        else {
            super.onBackPressed();
        }
    }

    private void createDataBase(){
        SQLiteDatabase myDatabase = this.openOrCreateDatabase("Drinktionary", MODE_PRIVATE, null);
    }
}
