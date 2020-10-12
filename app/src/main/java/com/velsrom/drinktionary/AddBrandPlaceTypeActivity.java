package com.velsrom.drinktionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddBrandPlaceTypeActivity extends AppCompatActivity {

    DatabaseHelper db;

    TextView addTitleTextView;
    TextView ubicSubtype;
    EditText nameEditText;
    EditText ubicSubtypeEditText;

    String placeBrandType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brand_place_type);

        addTitleTextView = findViewById(R.id.addTitleTextView);
        ubicSubtype = findViewById(R.id.optionTwoTextView);
        nameEditText = findViewById(R.id.nameEditText);
        ubicSubtypeEditText = findViewById(R.id.ubicSubtypeEditText);

        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        placeBrandType = intent.getStringExtra("ADDING_SECTION");
        addTitleTextView.setText("ADD " + placeBrandType);

        if(placeBrandType.equals("PLACE")){
            ubicSubtype.setText("Location");
        }
        else if(placeBrandType.equals("TYPE")){
            ubicSubtype.setText("Subtype");
        }
        else{ //IF "BRAND"
            ubicSubtype.setText("");
            ubicSubtypeEditText.setVisibility(View.GONE);
        }
    }

    public void add(View view){
        String name = nameEditText.getText().toString();
        String locationSubtype = ubicSubtypeEditText.getText().toString();

        if(placeBrandType.equals("PLACE")){
            db.insertPlaceData(name, locationSubtype);
        }
        else if(placeBrandType.equals("TYPE")){
           db.insertTypeData(name, locationSubtype);
        }
        else{
            db.insertBrandData(name);
        }
        Intent resultData = new Intent();
        resultData.putExtra("ADDING_SECTION", placeBrandType);
        setResult(RESULT_OK, resultData);
        finish();
    }

    public void backButton(View view){
        finish();
    }
}