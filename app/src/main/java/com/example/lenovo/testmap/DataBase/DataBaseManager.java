package com.example.lenovo.testmap.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.util.Base64;
import android.util.Log;

import com.example.lenovo.testmap.Adapters.BestOfAdapter;
import com.example.lenovo.testmap.Adapters.PageAdapter;
import com.example.lenovo.testmap.Controlers.Fragments.PageFragment;
import com.example.lenovo.testmap.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "GuideTouristique.db";
    private static final int DB_VERSION = 1;

    public DataBaseManager(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public void insertData( String nom, String description, String type, List<byte[]> images, SQLiteDatabase sqldb)
    {
        String tmpSQL = "yes";
        if(sqldb!=null)
            tmpSQL="no";

        Log.d("DatabaseInsert", "Is sqldb null: " + tmpSQL );


        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("description", description);
        values.put("type", type);

        String codesImages=Base64.encodeToString(images.get(0), Base64.DEFAULT);
        for(int i=1; i<images.size(); i++)
        {
            String img = Base64.encodeToString(images.get(i), Base64.DEFAULT);
            codesImages=codesImages+":::"+img;
        }

        Log.d("code", codesImages);

        values.put("image",codesImages);

        sqldb.insert("monuments", null, values);

        Log.d("DatabaseInsert", "insert done with image!");

    }

    public void insertData( String nom, String description, String type, List<byte[]> images)
    {
        SQLiteDatabase sqldb = getWritableDatabase();
        String tmpSQL = "yes";
        if(sqldb!=null)
            tmpSQL="no";

        Log.d("DatabaseInsert", "Is sqldb null: " + tmpSQL );

        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("description", description);
        values.put("type", type);

        String codesImages=Base64.encodeToString(images.get(0), Base64.DEFAULT);
        Log.d("Inside insertData", "images List size="+images.size());
        for(int i=1; i<images.size(); i++)
        {
            String img = Base64.encodeToString(images.get(i), Base64.DEFAULT);
            codesImages=codesImages+":::"+img;
        }

        Log.d("code", codesImages);

        values.put("image",codesImages);

        sqldb.insert("monuments", null, values);

        Log.d("DatabaseInsert", "insert done with image!");

    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String reqCreateMonuments = "create table monuments ( nom varchar(20) not null , "
                +      "description text not null , "
                +             "type varchar(10) not null , "
                +               "image text default null,"
                +                   "  _id INTEGER PRIMARY KEY AUTOINCREMENT )";

        Log.d("DatabaseCreate", "onCreate called !");
        db.execSQL(reqCreateMonuments);
        addData(db);

    }

    public void deleteData(int id)
    {
        Log.d("DatabaseDelete", "Deleting monument id = " + id);

        SQLiteDatabase sqldb = getWritableDatabase();
        sqldb.delete("monuments", "_id="+id, null);

        Log.d("DatabaseDelete", " Monument id = " + id + " deleted! ");

    }

    public void modifyData(int id, String nom, String description, String type, List<byte[]> images)
    {
        Log.d("DatabaseModify","Modifying id= " + id);

        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("description", description);
        values.put("type", type);

        String codesImages=Base64.encodeToString(images.get(0), Base64.DEFAULT);
        for(int i=1; i<images.size(); i++)
        {
            String img = Base64.encodeToString(images.get(i), Base64.DEFAULT);
            codesImages=codesImages+":::"+img;
        }

        Log.d("code", codesImages);

        values.put("image",codesImages);


        SQLiteDatabase sqldb = getWritableDatabase();
        sqldb.update("monuments", values, "_id="+id, null);

        Log.d("DatabaseModify"," id= " + id + " modified!");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    private void addData(SQLiteDatabase db)
    {
        List<byte[]> images = new ArrayList<byte[]>();

        Resources res = BestOfAdapter.context.getResources();
        Drawable d = res.getDrawable(R.drawable.jamaalefna1);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.jamaalefna2);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.jamaalefna3);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.jamaalefna4);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        insertData("Jamaa Lefna", "" +
                "Place publique avec commerçants, marchands et artistes " +
                "de rue fréquentée par les touristes et les locaux." +
                "", "nuit", images, db);

        images.clear();


        d = res.getDrawable(R.drawable.majorelle1);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.majorelle2);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.majorelle3);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.majorelle4);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.majorelle5);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        insertData("Jardin majorelle", "" +
                "Le jardin Majorelle est un jardin botanique touristique d'environ " +
                "3000 espèces sur près d'1 hectare, une villa art déco labellisée Maisons des " +
                "Illustres depuis 2011, et un musée de l'Histoire des Berbères, à Marrakech au Maroc" +
                "", "jour", images, db);
        images.clear();


        d = res.getDrawable(R.drawable.koutoubia1);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.koutoubia2);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.koutoubia3);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());
        insertData("Mosquée Koutoubia", "La mosquée Koutoubia est un édifice religieux" +
                " édifié au XIIᵉ siècle à Marrakech et représentatif de l'art des Almohades." +
                "", "jour", images, db);
        images.clear();

        d = res.getDrawable(R.drawable.menara1);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        d = res.getDrawable(R.drawable.menara2);
        bitmap = ((BitmapDrawable)d).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        images.add(stream.toByteArray());

        images.add(stream.toByteArray());
        insertData("Menara", "La Ménara est un vaste jardin planté d'oliviers aménagé sous la dynastie " +
                "des Almoahades à environ 45 minutes à pied de la place Jemaa el-Fna, au centre de Marrakech, au Maroc." +
                "", "jour", images, db);
        images.clear();





    }
}
