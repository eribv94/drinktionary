package com.velsrom.drinktionary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;

public class DrinktionaryActivity extends AppCompatActivity {

    ListView drinksListView;
    ArrayList<String> drinksNames;
    ArrayList<String> drinksPlaces;
    ArrayList<String> drinksBrands;
    ArrayList<String> drinksRating;
    ArrayList<String> drinksPrice;
    ArrayList<String> drinksId;
    ArrayList<String> drinksImagePath;

    Integer[] imageId = {
            R.drawable.bar,
            R.drawable.bar,
            R.drawable.bar,
            R.drawable.bar,
            R.drawable.bar, 0, 0, 0, 0, 0, 0, 0};

    //Filtros: Name - Place - Brand - Type - Score - Price
    TextView f1;
    TextView f2;
    TextView f3;
    TextView f4;
    TextView f5;
    TextView f6;

    String ascOrDesc = "ASC";
    String order = "NAME";

    DatabaseHelper dbh;

    CustomListView customListView;

    static final int REQUEST_CODE_NEW_DRINK_DATA = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinktionary);

        Spinner orderSpinner = findViewById(R.id.orderSpinner);
        Spinner enlistSpinner = findViewById(R.id.enlistSpinner);
        final SearchView drinkSearchView = findViewById(R.id.drinkSearchView);

        f1 = findViewById(R.id.f1TextView);
        f2 = findViewById(R.id.f2TextView);
        f3 = findViewById(R.id.f3TextView);
        f4 = findViewById(R.id.f4TextView);
        f5 = findViewById(R.id.f5TextView);
        f6 = findViewById(R.id.f6TextView);
        f1.setTypeface(null, Typeface.BOLD);

        final String orderOptions[] = {"ASC", "DESC "};
        final String enlistOptions[] = {"op1", "op2", "op3"};

        dbh = new DatabaseHelper(this);

        drinksNames = dbh.getDrinksData("NAME");
        drinksPlaces = dbh.getDrinksData("PLACE");
        drinksBrands = dbh.getDrinksData("BRAND");
        drinksRating = dbh.getDrinksData("SCORE");
        drinksPrice = dbh.getDrinksData("PRICE");
        drinksId = dbh.getDrinksData("ID");
        drinksImagePath = dbh.getDrinksData("IMAGE");

        drinksListView = findViewById(R.id.drinksListView);
        customListView = new CustomListView(this,
                drinksNames, drinksBrands, imageId, drinksRating, drinksPrice, drinksId);
        drinksListView.setAdapter(customListView);

        ArrayAdapter<String> orderDataAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderOptions);
        orderDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(orderDataAdapter);

        //                                                                                          Clicking a drink
        drinksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShowDrinkActivity.class);
                intent.putExtra("NAME", drinksNames.get(position));
                intent.putExtra("PLACE", drinksPlaces.get(position));
                intent.putExtra("BRAND", drinksBrands.get(position));
                intent.putExtra("SCORE", drinksRating.get(position));
                intent.putExtra("PRICE", drinksPrice.get(position));
                intent.putExtra("IMAGE", drinksImagePath.get(position));
                startActivity(intent);
            }
        });

        //                                                                                          Drink search bar
        drinkSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(DrinktionaryActivity.this, query, Toast.LENGTH_SHORT).show();
                drinkSearchView.setQuery("", false);
                drinkSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //                                                                                          Order of drinks
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Cuando se selecciona forma de ordenar asc o desc
                ascOrDesc = orderOptions[position];
                refreshData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<String> enlistDataAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, enlistOptions);
        enlistDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        enlistSpinner.setAdapter(enlistDataAdapter);

        enlistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Cuando se selecciona forma de ordenar
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    public void addDrink(View view){
        Intent addDrinkIntent = new Intent(getApplicationContext(), AddDrinkActivity.class);
        startActivityForResult(addDrinkIntent, REQUEST_CODE_NEW_DRINK_DATA);
        //              ACTUALIZAR LISTA SI SE ANADIO BEBIDA!!!!!!!!!!!!!!!!!!!
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_NEW_DRINK_DATA && resultCode == RESULT_OK) {
            Toast.makeText(this, "Adding successful", Toast.LENGTH_SHORT).show();
            refreshData();
        }
    }

    public void onFilterSelected(View view){
        TextView[] tvArr = {f1, f2, f3, f4, f5, f6};
        int filterTag = Integer.parseInt(view.getTag().toString());
        for (int i = 0; i < tvArr.length; i++) {
            tvArr[i].setTypeface(null, Typeface.NORMAL);
        }
        tvArr[filterTag].setTypeface(null, Typeface.BOLD);

        /*
        DAR OPCIONES DE QUE TIPOS SE QUIERE
        EJ. EN PRECIOS QUE RANGO QUIERES, O EN CALIFICACION SOLO LAS DE 4 ESTRELLAS O BEBIDAS SOLO CERVEZAS, ETC
         */

        switch (filterTag){
            case 0:
                order = "NAME";
                break;
            case 1:
                order = "PLACE";
                break;
            case 2:
                order = "BRAND";
                break;
            case 3:
                order = "TYPE";
                break;
            case 4:
                order = "SCORE";
                break;
            case 5:
                order = "PRICE";
                break;
        }
        refreshData();
    }

    public void refreshData(){
        drinksNames.clear();
        drinksPlaces.clear();
        drinksBrands.clear();
        drinksRating.clear();
        drinksPrice.clear();

        drinksNames = dbh.getDrinksData("NAME", order, ascOrDesc);
        drinksPlaces = dbh.getDrinksData("PLACE", order, ascOrDesc);
        drinksBrands = dbh.getDrinksData("BRAND", order, ascOrDesc);
        drinksRating = dbh.getDrinksData("SCORE", order, ascOrDesc);
        drinksPrice = dbh.getDrinksData("PRICE", order, ascOrDesc);
        drinksId = dbh.getDrinksData("ID", order, ascOrDesc);

        customListView = new CustomListView(this, drinksNames, drinksBrands, imageId, drinksRating, drinksPrice, drinksId);
        drinksListView.setAdapter(customListView);
    }

    public void goBack(View view){
        finish();
    }
}