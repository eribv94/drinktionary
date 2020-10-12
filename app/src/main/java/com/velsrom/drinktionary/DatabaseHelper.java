package com.velsrom.drinktionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Drinktionary.db";

    public static final String DRINKS_TABLE = "drinks_table";
    public static final String BRAND_TABLE = "brand_table";
    public static final String PLACE_TABLE = "place_table";
    public static final String TYPE_TABLE = "type_table";

    public static final String ID_DRINK = "id";
    public static final String NAME_DRINK = "name";
    public static final String PLACE = "place";
    public static final String BRAND = "brand";
    public static final String SCORE = "score";
    public static final String TYPE = "type";
    public static final String PRICE = "price";
    public static final String IMAGE = "image";

    public static final String ID_BRAND = "id";
    public static final String NAME_BRAND = "name";

    public static final String ID_PLACE = "id";
    public static final String NAME_PLACE = "name";
    public static final String LOCATION = "location";

    public static final String ID_TYPE = "id";
    public static final String NAME_TYPE = "name";
    public static final String SUBNAME_TYPE = "subname";


//    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //Crea base de datos con sus tablas respectivas
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PLACE_TABLE + " (" +
                ID_PLACE +      " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NAME_PLACE +    " VARCHAR, " +
                LOCATION +      " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + BRAND_TABLE + " (" +
                ID_BRAND +      " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NAME_BRAND +    " VARCHAR)");


        db.execSQL("CREATE TABLE IF NOT EXISTS " + TYPE_TABLE + " (" +
                ID_TYPE +       " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NAME_TYPE +     " VARCHAR, " +
                SUBNAME_TYPE +  " VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DRINKS_TABLE + " (" +
                ID_DRINK +      " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NAME_DRINK +    " VARCHAR, " +
                PLACE +         " VARCHAR, " +  //FK
                BRAND +         " VARCHAR, " +  //FK
                SCORE +         " INTEGER(1), " +
                TYPE +          " VARCHAR, " +  //FK
                PRICE +         " INTEGER(3), " +
                IMAGE +         " VARCHAR)");
//                + " FOREIGN KEY (" + PLACE + ") REFERENCES " + PLACE_TABLE + "(" + ID_PLACE + "),"
//                + " FOREIGN KEY (" + BRAND + ") REFERENCES " + BRAND_TABLE + "(" + ID_BRAND + "),"
//                + " FOREIGN KEY (" + TYPE + ") REFERENCES " + TYPE_TABLE + "(" + ID_TYPE + "));"
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertDrinkData(String name, String place, String brand, int score,
                                   String type, int price, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_DRINK, name);
        contentValues.put(PLACE, place);
        contentValues.put(BRAND, brand);
        contentValues.put(SCORE, score);
        contentValues.put(TYPE, type);
        contentValues.put(PRICE, price);
        contentValues.put(IMAGE, image);
        long result = db.insert(DRINKS_TABLE, null, contentValues);
        if(result == -1){
            //Si no hace la insersion, arroja -1 y mano falso para dar a entender esto.
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertPlaceData(String name, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_PLACE, name);
        contentValues.put(LOCATION, location);
        long result = db.insert(PLACE_TABLE, null, contentValues);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertBrandData(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_BRAND, name);
        long result = db.insert(BRAND_TABLE, null, contentValues);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertTypeData(String name, String subname){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_TYPE, name);
        contentValues.put(SUBNAME_TYPE, subname);
        long result = db.insert(TYPE_TABLE, null, contentValues);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getAllData(String dataTable, String orderBy, String ascDesc){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = null;
        String query = "";
        if (dataTable.equals("PLACE")) {
            query = "SELECT * FROM " + PLACE_TABLE;
        }
        else if(dataTable.equals("BRAND")){
            query = "SELECT * FROM " + BRAND_TABLE;
        }
        else if(dataTable.equals("TYPE")){
            query = "SELECT * FROM " + TYPE_TABLE;
        }
        else if(dataTable.equals("DRINK")){
            query =  "SELECT * FROM " + DRINKS_TABLE;
        }
        query = query + " ORDER BY " + orderBy + " " + ascDesc;
        result = db.rawQuery(query, null);
        if(result == null){ Log.i("GET ALL DATA METHOD","ERROR ENTERING WHICH TABLE WANT TO USE");}
        return result;
    }

    public ArrayList<String> getDrinksData(String column, String orderBy, String ascDesc){
        Cursor cursorResult = getAllData("DRINK", orderBy, ascDesc);
        ArrayList<String> list = new ArrayList<>();
        int columnNum = 1;
        if(column.equals("NAME")){
            columnNum = 1;
        }
        else if(column.equals("PLACE")){
            columnNum = 2;
        }
        else if(column.equals("BRAND")){
            columnNum = 3;
        }
        else if(column.equals("SCORE")){
            columnNum = 4;
        }
        else if(column.equals("PRICE")){
            columnNum = 6;
        }
        if(cursorResult.getCount() == 0){
            return null;
        }else{
            while(cursorResult.moveToNext()){
                list.add(cursorResult.getString(columnNum));
            }
        }
        return list;
    }

    public ArrayList<String> getDrinksData(String column){
        Cursor cursorResult = getAllData("DRINK", "NAME", "ASC");
        ArrayList<String> list = new ArrayList<>();
        int columnNum = 1;
        if(column.equals("NAME")){
            columnNum = 1;
        }
        else if(column.equals("PLACE")){
            columnNum = 2;
        }
        else if(column.equals("BRAND")){
            columnNum = 3;
        }
        else if(column.equals("SCORE")){
            columnNum = 4;
        }
        else if(column.equals("PRICE")){
            columnNum = 6;
        }
        if(cursorResult.getCount() == 0){
            return null;
        }else{
            while(cursorResult.moveToNext()){
                list.add(cursorResult.getString(columnNum));
            }
        }
        return list;
    }

    public void createData(){
        //SE EJECUTARA 1 SOLA VEZ, LA PRIMERA VEZ QUE SE ABRA LA APLICACION PARA CREARSE DATOS
        //      Bebidas
        insertDrinkData("Test Beer", "Test Place", "Test Brand", 5,"Cerveza", 99, "");
        insertDrinkData("Perro del Mar", "BCB", "Wendlandt", 5,"Cerveza", 75, "");
        insertDrinkData("Sirena", "Aguamala", "Aguamala", 4,"Cerveza", 70, "");

        //      Lugares
        insertPlaceData("BCB","Calle Orizaba 10335, Neidhart, 22020 Tijuana, B.C.");
        insertPlaceData("Bierhalle","Blvrd Gral Rodolfo Sánchez Taboada 10521, Defensores de Baja California, 22014 Tijuana, B.C.");
        insertPlaceData("Telefonica Gastro Park","Blvd. Agua Caliente 8924, Zonaeste, 22000 Tijuana, B.C.");

        //      Marcas
        insertBrandData("Wendlandt");
        insertBrandData("Insurgente");
        insertBrandData("Aguamala");
        insertBrandData("Tecate");
        insertBrandData("Bohemia");
        insertBrandData("Modelo");

        //      Tipos
        insertTypeData("Cerveza", "IPA");
        insertTypeData("Cerveza", "Ale");
        insertTypeData("Cerveza", "");
        insertTypeData("Agua Fresca", "");
    }
}