package com.example.lenovo.testmap.Controlers.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.testmap.Adapters.BestOfAdapter;
import com.example.lenovo.testmap.Controlers.Fragments.FormModifyingDialogFragment;
import com.example.lenovo.testmap.Controlers.Fragments.PageFragment;
import com.example.lenovo.testmap.DataBase.DataBaseManager;
import com.example.lenovo.testmap.Models.Data.Monuments;
import com.example.lenovo.testmap.R;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog extends Dialog
{
    private Context context;
    private Button delButton;
    private Button modButton;
    private TextView placeName;
    private TextView placeDesc;
    private Monuments monument;
    private LinearLayout linearLayout;

    private Activity activity;

    private BestOfAdapter boa;
    public CustomDialog(BestOfAdapter boa, Monuments monument, Context context, Activity activity)
    {
        super(context);
        this.activity=activity;
        this.setContentView(R.layout.custom_dialog_unit);
        this.boa=boa;
        this.context=context;
        this.delButton=findViewById(R.id.delete);
        this.modButton=findViewById(R.id.modify);
        this.placeDesc=findViewById(R.id.PlaceDesc);
        this.placeName=findViewById(R.id.PlaceName);
        this.linearLayout=findViewById(R.id.linearLayoutCustomDialog);
        this.monument=monument;

        this.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                initDelListener();
            }
        });
        this.modButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initModListener();
            }
        });
    }

    public void initDelListener()
    {
        int id = monument.getId();
        ArrayList<Monuments> monuments = BestOfAdapter.getMonuments();
        int indexInMonument=-1, i=0;
        boolean trouv=false;
        while( i<id && trouv==false)
        {
         Monuments m = monuments.get(i);
         if(m.getId()==id) {
             indexInMonument = i;
             trouv=true;
         }
         i++;
        }
        BestOfAdapter.getMonuments().remove(indexInMonument);
        PageFragment.getDb().deleteData(id);

        this.hide();
        boa.notifyDataSetChanged();


    }

    public void initModListener()
    {
        FormModifyingDialogFragment dialog = new FormModifyingDialogFragment();
        dialog.initParams(boa,monument);


        dialog.show(activity.getFragmentManager(), "dialog");

        this.hide();
    }

    public void setPlaceName(String placeName)
    {
        this.placeName.setText(placeName);
    }

    public void setPlaceDesc(String desc)
    {
        this.placeDesc.setText(desc);
    }

    public void setImg(List<byte[]> imgList)
    {
        Log.d("SetImg CustomDialog", "imgList.size="+imgList.size());
        for(int i=0; i<imgList.size(); i++)
        {
            ImageView imgView = new ImageView(context);
            Glide.with(context)
                    .load(imgList.get(i))
                    .asBitmap()
                    .fitCenter()
                    .into(imgView);
            linearLayout.addView(imgView);
        }
    }
}
