package com.velsrom.drinktionary;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowDrinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_drink);

        TextView nameView = findViewById(R.id.nameView);
        TextView brandView = findViewById(R.id.brandView);
        TextView placeView = findViewById(R.id.placeView);
        TextView priceView = findViewById(R.id.priceView);
        TextView scoreView = findViewById(R.id.scoreView);
        ImageView imageView = findViewById(R.id.drinkImageView);

        String name      = getIntent().getExtras().getString("NAME");
        String place     = getIntent().getExtras().getString("PLACE");
        String brand     = getIntent().getExtras().getString("BRAND");
        String score     = getIntent().getExtras().getString("SCORE");
        String price     = getIntent().getExtras().getString("PRICE");
        String imagePath = getIntent().getExtras().getString("IMAGE");

        nameView.setText(name);
        placeView.setText(place);
        brandView.setText(brand);
        scoreView.setText(score);
        priceView.setText(price);
        if(!imagePath.equals("")){
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(myBitmap);
        }
    }
}