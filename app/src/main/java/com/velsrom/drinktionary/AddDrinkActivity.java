package com.velsrom.drinktionary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddDrinkActivity extends AppCompatActivity {

    DatabaseHelper db;

    EditText drinkNameEditText;
    EditText priceEditText;
    SeekBar scoreSeekbar;
    AutoCompleteTextView placeAutocomplete;
    AutoCompleteTextView brandAutocomplete;
    Spinner typeSpinner;

    String imagepath = "";
    OutputStream outputStream;

    ArrayList<String> places;
    ArrayList<String> brands;
    ArrayList<String> types;
    ArrayAdapter<String> placeAdapter;
    ArrayAdapter<String> brandAdapter;
    ArrayAdapter<String> typeAdapter;

    static final int REQUEST_CODE_NEW_DRINK_DATA = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);

        drinkNameEditText = findViewById(R.id.drinkNameEditText);
        priceEditText = findViewById(R.id.priceEditText);
        scoreSeekbar = findViewById(R.id.scoreSeekBar);
        placeAutocomplete = findViewById(R.id.placeAutoCompleteTextView);
        brandAutocomplete = findViewById(R.id.brandAutoCompleteTextView);
        typeSpinner = findViewById(R.id.typeSpinner);

        db = new DatabaseHelper(this);

        //Conctar BD a las sugerencias
        Cursor placesData = db.getAllData("PLACE", "NAME", "ASC");
        Cursor brandData = db.getAllData("BRAND", "NAME", "ASC");
        Cursor typeData = db.getAllData("TYPE", "NAME", "ASC");
        places = new ArrayList<>();
        brands = new ArrayList<>();
        types = new ArrayList<>();
        if(placesData.getCount() == 0){ //arreglar
            return;
        }
        else{
            while (placesData.moveToNext()){
                places.add(placesData.getString(1)); //Column index 1 es el nombre de lugares
            }
            while (brandData.moveToNext()){
                brands.add(brandData.getString(1)); //Column index 1 es el nombre de las marcas
            }
            while (typeData.moveToNext()){
                StringBuffer typeStr = new StringBuffer();
                typeStr.append(typeData.getString(1));
                if(!typeData.getString(2).equals("")){
                    typeStr.append(" - " + typeData.getString(2));
                }
                types.add(typeStr.toString());
            }
        }

        placeAdapter = new ArrayAdapter<>(AddDrinkActivity.this, android.R.layout.simple_list_item_1, places);
        brandAdapter = new ArrayAdapter<>(AddDrinkActivity.this, android.R.layout.simple_list_item_1, brands);
        typeAdapter = new ArrayAdapter<>(AddDrinkActivity.this, android.R.layout.simple_list_item_1, types);
        placeAutocomplete.setAdapter(placeAdapter);
        brandAutocomplete.setAdapter(brandAdapter);
        typeSpinner.setAdapter(typeAdapter);
    }

    public void addNewDrink(View view){
        String name = drinkNameEditText.getText().toString();
        String priceText = priceEditText.getText().toString();
        int score = scoreSeekbar.getProgress();
        String place= placeAutocomplete.getText().toString();
        String brand = brandAutocomplete.getText().toString();
        String type = typeSpinner.getSelectedItem().toString();
        int price = (priceEditText.getText().toString().equals("")) ? 0 : Integer.parseInt(priceEditText.getText().toString());
        String image = imagepath;

        if(name.equals("") || priceText.equals("")){
            Toast.makeText(this, "Add data correctly", Toast.LENGTH_SHORT).show();
        }
        else {
            boolean insertionStatus = db.insertDrinkData(name, place, brand, score, type, price, image);
            if(insertionStatus){
                Intent resultData = new Intent();
                setResult(RESULT_OK, resultData);
                finish();
            }
            else {
                Toast.makeText(this, "Something went wrong adding data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addPlaceBrandType(View view){
        String addWhat = view.getTag().toString();
        Intent addPlaceBrandTypeIntent = new Intent(this, AddBrandPlaceTypeActivity.class);
        if (addWhat.equals("add_place")){
            addPlaceBrandTypeIntent.putExtra("ADDING_SECTION", "PLACE");
        }
        else if (addWhat.equals("add_type")){
            addPlaceBrandTypeIntent.putExtra("ADDING_SECTION", "TYPE");
        }
        else{ //"add_brand"
            addPlaceBrandTypeIntent.putExtra("ADDING_SECTION", "BRAND");
        }

        startActivityForResult(addPlaceBrandTypeIntent, REQUEST_CODE_NEW_DRINK_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //REFRESCAR TODAS LAS TABLAS PARA LA BUSQUEDA!!!!!!

        if (requestCode == REQUEST_CODE_NEW_DRINK_DATA && resultCode == RESULT_OK) {
            Toast.makeText(this, "Adding successful", Toast.LENGTH_SHORT).show();

            String placeBrandType = data.getStringExtra("ADDING_SECTION");
            if (placeBrandType.equals("PLACE")) {
                places.clear();
                Cursor placesData = db.getAllData("PLACE", "PLACE", "ASC");
                while (placesData.moveToNext()) {
                    places.add(placesData.getString(1));
                }
                placeAdapter.notifyDataSetChanged();
            } else if (placeBrandType.equals("BRAND")) {
                brands.clear();
                Cursor brandData = db.getAllData("BRAND", "BRAND", "ASC");
                while (brandData.moveToNext()) {
                    brands.add(brandData.getString(1));
                }
                brandAdapter.notifyDataSetChanged();
            } else if (placeBrandType.equals("TYPE")) {
                types.clear();
                Cursor typeData = db.getAllData("TYPE", "TYPE", "ASC");
                while (typeData.moveToNext()) {
                    StringBuffer typeStr = new StringBuffer();
                    typeStr.append(typeData.getString(1));
                    if (!typeData.getString(2).equals("")) {
                        typeStr.append(" - " + typeData.getString(2));
                    }
                    types.add(typeStr.toString());
                }
                typeAdapter.notifyDataSetChanged();
            }
        } else {
            System.out.println(data);
            System.out.println(data.getExtras().get("data"));

            if (resultCode == RESULT_OK && data != null) {
                File photoFile = new File(getFilesDir() + "/Drinks/");
                if (!photoFile.isFile()) {
                    boolean mkdir = photoFile.mkdir();
                    if (mkdir) {
                        Log.i("File creation", "Success: " + mkdir);
                    } else {
                        Log.i("File creation", "Already created: " + photoFile);
                    }
                }
                File file = new File(photoFile, System.currentTimeMillis() + ".jpg");
                try {
                    outputStream = new FileOutputStream(file);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = null;
                if(requestCode == 1){
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    bitmap = (Bitmap) data.getExtras().get("data");
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                Toast.makeText(getApplicationContext(), "Image Saved: " + file.getPath(), Toast.LENGTH_SHORT).show();
                Log.i("File path: ", file.getPath());

                imagepath = file.getPath();
            }
        }
    }

    public void addImage(View view){
        //Aqui declaras el path de la imagen para poder guardarla en la base de datos del cel
        // y la declaras en la variable "image path"
        String cameraOrFile = view.getTag().toString();
        if(cameraOrFile.equals("camera")){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, 0);
        }
        else {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 1);
        }

        imagepath = "";
    }

    public void goBack(View view){
        finish();
    }
}