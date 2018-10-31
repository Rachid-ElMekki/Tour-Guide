package com.example.lenovo.testmap.Controlers.Dialogs;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.testmap.Adapters.BestOfAdapter;
import com.example.lenovo.testmap.Controlers.Fragments.PageFragment;
import com.example.lenovo.testmap.Models.Data.Monuments;
import com.example.lenovo.testmap.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FormModifyingDialog extends Dialog
{


    private Monuments currentM;
    private TextView nomM;
    private TextView descM;
    private ImageView imageM;
    private Button modifyButton;
    private Button modifyImageButton;
    private RadioButton radioNuit;
    private RadioButton radioJour;
    private RadioGroup radios;
    private Button delImages;

    private Context context;

    private LinearLayout linearLayout;

    private BestOfAdapter boa;

    private List<byte[]> imagesList;


    public FormModifyingDialog(BestOfAdapter boa, Context context, Monuments monument)
    {
        super(context);
        this.setContentView(R.layout.form_modif_dialog);

        this.nomM = findViewById(R.id.txtModifName);
        this.descM = findViewById(R.id.descM);
        this.imageM= findViewById(R.id.imageM);
        this.modifyImageButton= findViewById(R.id.ModifyImage);
        this.modifyButton= findViewById(R.id.modifyButton);
        this.radios = findViewById(R.id.radioGroupModif);
        this.radioJour = findViewById(R.id.jourM);
        this.radioNuit = findViewById(R.id.nuitM);
        this.linearLayout= findViewById(R.id.linearLayoutFormModifDialog);
        this.delImages=findViewById(R.id.delImagesDialog);

        this.currentM=monument;
        this.context=context;
        this.boa=boa;

        initView();


        this.modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initConfimation();

            }
        });

        delImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesList.clear();
                linearLayout.removeAllViews();

                Resources res = getContext().getResources();
                Drawable d = res.getDrawable(R.drawable.nophoto);
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                imageM.setImageBitmap(bitmap);

                linearLayout.addView(imageM);
            }
        });

    }


    public void initConfimation()
    {
        int id = currentM.getId();
        String nom = nomM.getText().toString();
        String desc = descM.getText().toString();
        RadioButton r = findViewById(radios.getCheckedRadioButtonId());
        String type;
        if(r==radioJour)
        {
            type = "jour";
        }
        else
        {
            type = "nuit";
        }
        List<byte[]> imagesList = new ArrayList<byte[]>();
        for(int i=1; i<linearLayout.getChildCount(); i++)
        {//Dans tout les cas il n'y aura que des ImageView
            Log.d("retrieving images", "int i=" + i);
            imagesList.add(imageViewWithGlideDToByte((ImageView) linearLayout.getChildAt(i)));
        }

        if(imagesList.size()==0)
        {
            Log.d("Images", "No images provided");
            Resources res = getContext().getResources();
            Drawable d = res.getDrawable(R.drawable.nophoto);
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            imagesList.add(stream.toByteArray());
        }

        PageFragment.getDb().modifyData(id, nom, desc, type, imagesList);

        boa.modifEnt(id, nom, desc, type, imagesList);


        this.dismiss();
        boa.notifyDataSetChanged();
    }

    public byte[] imageViewWithGlideDToByte(final ImageView imgView)
    {
        Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable().getCurrent()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    public void initView()
    {
        this.getNomM().setText(currentM.getNom());
        this.getDescM().setText(currentM.getDescription());
        imagesList = currentM.getImages();
        Glide.with(getContext())
                .load(imagesList.get(0))
                .asBitmap()
                .fitCenter()
                .into(imageM);
        for(int i=1; i<imagesList.size(); i++)
        {
            ImageView imgView = new ImageView(getContext());
            Glide.with(getContext())
                    .load(imagesList.get(i))
                    .asBitmap()
                    .fitCenter()
                    .into(imgView);
            linearLayout.addView(imgView);
        }


        if(currentM.getType().equals("jour"))
        {
            this.getRadioJour().setChecked(true);
            this.getRadioNuit().setChecked(false);
        }
        else
        {
            this.getRadioJour().setChecked(false);
            this.getRadioNuit().setChecked(true);
        }
    }

    public RadioButton getRadioNuit() {
        return radioNuit;
    }

    public void setRadioNuit(RadioButton radioNuit) {
        this.radioNuit = radioNuit;
    }

    public RadioButton getRadioJour() {
        return radioJour;
    }

    public void setRadioJour(RadioButton radioJour) {
        this.radioJour = radioJour;
    }

    public TextView getNomM() {
        return nomM;
    }

    public void setNomM(TextView nomM) {
        this.nomM = nomM;
    }

    public TextView getDescM() {
        return descM;
    }

    public void setDescM(TextView descM) {
        this.descM = descM;
    }

    public ImageView getImageM() {
        return imageM;
    }

    public void setImageM(ImageView imageM) {
        this.imageM = imageM;
    }

    public Button getModifyButton() {
        return modifyButton;
    }

    public void setModifyButton(Button modifyButton) {
        this.modifyButton = modifyButton;
    }

    public RadioGroup getRadios() {
        return radios;
    }

    public void setRadios(RadioGroup radios) {
        this.radios = radios;
    }

    public Button getModifyImageButton() {
        return modifyImageButton;
    }

    public void setModifyImageButton(Button modifyImageButton) {
        this.modifyImageButton = modifyImageButton;
    }

    public List<byte[]> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<byte[]> imagesList) {
        this.imagesList = imagesList;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }
}
