package com.velsrom.drinktionary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomListView extends ArrayAdapter<String> {

    private ArrayList<String> drinks;
    private ArrayList<String> description;
    private Integer[] imageId;
    private ArrayList<String> rating;
    private ArrayList<String> price;
    private Activity context;
    private ArrayList<String> id;

    public CustomListView(Activity context, ArrayList<String> drinks, ArrayList<String> description,
                          Integer[] imageId, ArrayList<String> rating, ArrayList<String> price, ArrayList<String> id) {
        super(context, R.layout.listview_layout, drinks);
        this.id = id;
        this.context = context;
        this.drinks = drinks;
        this.description = description;
        this.imageId = imageId;
        this.rating = rating;
        this.price = price;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.drinkImageView.setImageResource(imageId[position]);
        viewHolder.drinkNameTextView.setText(drinks.get(position));
        viewHolder.drinkDescriptionTextView.setText(description.get(position));
        viewHolder.drinkPriceTextView.setText(price.get(position));
        viewHolder.drinkScoreTextView.setText(rating.get(position));
        return r;
//        return super.getView(position, convertView, parent);
    }

    class ViewHolder{
        TextView drinkNameTextView;
        TextView drinkDescriptionTextView;
        TextView drinkScoreTextView;
        TextView drinkPriceTextView;
        ImageView drinkImageView;

        ViewHolder(View v){
            drinkNameTextView = v.findViewById(R.id.drinkName);
            drinkDescriptionTextView = v.findViewById(R.id.drinkDescription);
            drinkImageView = v.findViewById(R.id.imageView);
            drinkPriceTextView = v.findViewById(R.id.priceTextView);
            drinkScoreTextView = v.findViewById(R.id.ratingTextView);
        }
    }
}



