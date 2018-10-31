package com.example.lenovo.testmap.Adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.testmap.Controlers.Dialogs.CustomDialog;
import com.example.lenovo.testmap.DataBase.DataBaseManager;
import com.example.lenovo.testmap.Models.Data.Monuments;
import com.example.lenovo.testmap.R;

import java.util.ArrayList;
import java.util.List;

public class BestOfAdapter extends RecyclerView.Adapter<BestOfAdapter.MyViewHolder> {

private static ArrayList<Monuments> monuments;
public static Context context;
private DataBaseManager db;
private Activity activity;

public BestOfAdapter(DataBaseManager db, Context c, Activity activity)
{

    context = c;
    monuments = new ArrayList<Monuments>();
    this.db=db;
    this.activity=activity;

    fillMonuments(db);


}

    public DataBaseManager getDb() {
        return db;
    }

    public static void setMonuments(ArrayList<Monuments> monuments) {
        BestOfAdapter.monuments = monuments;
    }

    public static ArrayList<Monuments> getMonuments() {
        return monuments;
    }

    public void fillMonuments(DataBaseManager db)
    {
    monuments.clear();

    String req = "Select * from monuments";
    Log.d("Database", "select called !");
    Cursor cursor = db.getReadableDatabase().rawQuery(req, null);
    Log.d("Database", "select done!");
    cursor.moveToFirst();
    while(!cursor.isAfterLast() && cursor !=null)
    {
        Log.d("Database", "adding monument to list");
        String nomMonument = "null";
        if(!cursor.isNull(0))
            nomMonument = cursor.getString(0);
        String descMonument = "null";
        if(!cursor.isNull(1))
            descMonument = cursor.getString(1);
        String typeMonumemnt = "null";
        if(!cursor.isNull(2))
            typeMonumemnt = cursor.getString(2);
        String imageCodes="null";
        if(!cursor.isNull(3))
            imageCodes = cursor.getString(3);

        Log.d("codes", imageCodes);

        String[] strings=imageCodes.split(":::");

        for(int k=0; k<strings.length; k++)
            Log.d("Strings" + k, strings[k]);
        List<byte[]> imagesList = new ArrayList<byte[]>();
        for(int i=0; i<strings.length; i++)
        {
            imagesList.add(Base64.decode(strings[i], Base64.DEFAULT));
        }

        int idMonument=-1;
        if(!cursor.isNull(4))
            idMonument = cursor.getInt(4);
        Log.d("id","idMonument="+idMonument);
        Monuments m;
        if(imagesList == null)
        {
            m = new Monuments(idMonument, nomMonument,descMonument,typeMonumemnt);
        }
        else
        {
            m = new Monuments(idMonument, nomMonument,descMonument,typeMonumemnt, imagesList);
        }

        monuments.add(m);
        Log.d("Database", "monument added to list: " + m.toString());

        cursor.moveToNext();
    }
}

@Override
public int getItemCount() {
        return monuments.size();
        }

@Override
public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new MyViewHolder(view);
        }

@Override
public void onBindViewHolder(MyViewHolder holder, int position) {
        Monuments monument = monuments.get(position);
        holder.display(monument);
        }

    public void modifyListSpinner(String type, DataBaseManager db)
    {
        if (type != "tout") {
            monuments.clear();
            String req = "Select * from monuments where type='" + type + "'";
            Log.d("Database modifyLis", "select called !");
            Cursor cursor = db.getReadableDatabase().rawQuery(req, null);
            Log.d("Database modifyLis", "select done!");
            cursor.moveToFirst();
            while (!cursor.isAfterLast() && cursor != null) {
                Log.d("Database modifyLis", "adding monument to list");
                String nomMonument = "null";
                if (!cursor.isNull(0))
                    nomMonument = cursor.getString(0);
                String descMonument = "null";
                if (!cursor.isNull(1))
                    descMonument = cursor.getString(1);
                String typeMonumemnt = "null";
                if (!cursor.isNull(2))
                    typeMonumemnt = cursor.getString(2);
                String imageCodes="null";
                if(!cursor.isNull(3))
                    imageCodes = cursor.getString(3);

                Log.d("codes", imageCodes);

                String[] strings=imageCodes.split(":::");

                for(int k=0; k<strings.length; k++)
                    Log.d("Strings" + k, strings[k]);
                List<byte[]> imagesList = new ArrayList<byte[]>();
                for(int i=0; i<strings.length; i++)
                {
                    imagesList.add(Base64.decode(strings[i], Base64.DEFAULT));
                }

                int idMonument = cursor.getInt(4);
                Log.d("DATABASE", "Row index: " + cursor.getInt(4) + " idMonument= " + idMonument);
                Monuments m;
                if(imagesList == null)
                {
                    m = new Monuments(idMonument, nomMonument,descMonument,typeMonumemnt);
                }
                else
                {
                    m = new Monuments(idMonument, nomMonument,descMonument,typeMonumemnt, imagesList);
                }

                monuments.add(m);
                Log.d("Database modifyLis", "monument added to list: " + m.toString());

                cursor.moveToNext();
            }
        }

        if (type.equals("tout"))
        {
            fillMonuments(db);
        }

    }

    public void modifyListSearch(String txt, DataBaseManager db) {
            monuments.clear();
            String req = "Select * from monuments where nom LIKE '%" + txt + "%'";
            Log.d("Database modifyLis", "select called !");
            Cursor cursor = db.getReadableDatabase().rawQuery(req, null);
            Log.d("Database modifyLis", "select done!");
            cursor.moveToFirst();
            while (!cursor.isAfterLast() && cursor != null) {
                Log.d("Database modifyLis", "adding monument to list");
                String nomMonument = "null";
                if (!cursor.isNull(0))
                    nomMonument = cursor.getString(0);
                String descMonument = "null";
                if (!cursor.isNull(1))
                    descMonument = cursor.getString(1);
                String typeMonumemnt = "null";
                if (!cursor.isNull(2))
                    typeMonumemnt = cursor.getString(2);
                String imageCodes="null";
                if(!cursor.isNull(3))
                    imageCodes = cursor.getString(3);

                Log.d("codes", imageCodes);

                String[] strings=imageCodes.split(":::");

                for(int k=0; k<strings.length; k++)
                    Log.d("Strings" + k, strings[k]);
                List<byte[]> imagesList = new ArrayList<byte[]>();
                for(int i=0; i<strings.length; i++)
                {
                    imagesList.add(Base64.decode(strings[i], Base64.DEFAULT));
                }

                int idMonument = cursor.getInt(4);
                Log.d("DATABASE", "Row index: " + cursor.getInt(4) + " idMonument= " + idMonument);
                Monuments m;
                if(imagesList == null)
                {
                    m = new Monuments(idMonument, nomMonument,descMonument,typeMonumemnt);
                }
                else
                {
                    m = new Monuments(idMonument, nomMonument,descMonument,typeMonumemnt, imagesList);
                }

                monuments.add(m);
                Log.d("Database modifyLis", "monument added to list: " + m.toString());

                cursor.moveToNext();
            }
            cursor.close();

    }

    public void modifEnt(int id, String nom, String desc, String type, List<byte[]> imagesList)
    {
        boolean trouv=false;
        int i=0;
        int indexTrouv=-1;
        while(trouv==false && i<monuments.size())
        {
            if(id==monuments.get(i).getId()) {
                trouv = true;
                indexTrouv = i;
            }
            i++;
        }
        monuments.get(indexTrouv).setNom(nom);
        monuments.get(indexTrouv).setDescription(desc);
        monuments.get(indexTrouv).setType(type);
        monuments.get(indexTrouv).setImages(imagesList);

        this.notifyDataSetChanged();
    }

public class MyViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView description;
    private final LinearLayout linearLayout;

    private Monuments current;

    public MyViewHolder(final View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        description =  itemView.findViewById(R.id.description);
        linearLayout=itemView.findViewById(R.id.linearLayoutListCell);

       itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog dialog = new CustomDialog(PageAdapter.getPageFragment().getBestOfAdapter(), current, itemView.getContext(), activity);
                dialog.setImg(current.getImages());
                dialog.setPlaceName(current.getNom());
                dialog.setPlaceDesc(current.getDescription());
                dialog.show();
            }
        });
    }

    public void display(Monuments monument)
    {
        current = monument;
        name.setText(monument.getNom());
        description.setText(monument.getDescription());
        Log.d("BestOfAdapter display", "monument.getImages().size()= " + monument.getImages().size() +
                "  monument id=" + current.getId());

        for(int i=0; i<monument.getImages().size(); i++)
        {
            ImageView imgView = new ImageView(context);
            imgView.setAdjustViewBounds(true);
            imgView.setMaxWidth(1100);
            imgView.setMaxHeight(500);
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context)
                    .load(monument.getImages().get(i))
                    .asBitmap()
                    .centerCrop()
                    .into(imgView);
            linearLayout.addView(imgView);

        }
    }
}


}