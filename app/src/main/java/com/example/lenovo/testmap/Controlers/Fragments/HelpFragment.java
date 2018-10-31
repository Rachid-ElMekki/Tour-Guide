package com.example.lenovo.testmap.Controlers.Fragments;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.testmap.Adapters.BestOfAdapter;
import com.example.lenovo.testmap.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class HelpFragment extends Fragment {

    public final static int REQUEST_CODE_GALLERY = 999;

    private View v;
    private Button callTaxi;
    private Button callAmbulance;
    private Button callPolice;
    private EditText nomMonument;
    private EditText descMonument;
    private ImageView image;
    private Button ajouterImage;
    private Button ajouter;
    private RadioGroup radios;
    private RadioButton jour;
    private RadioButton nuit;
    private LinearLayout linearLayout;
    private TextView confSucces;
    private Button delImages;

    private List<byte[]> imagesList = new ArrayList<byte[]>();

    public static HelpFragment newInstance() {
        return (new HelpFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_help, container, false);

        initCallButtons();
        initAddPart();


        return v;
    }


    public void initCallButtons() {
        callTaxi = v.findViewById(R.id.callTaxi);
        callTaxi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0524409494"));
                startActivity(appel);

            }
        });

        callPolice = v.findViewById(R.id.callPolice);
        callPolice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:19"));
                startActivity(appel);

            }
        });

        callAmbulance = v.findViewById(R.id.callAmbulance);
        callAmbulance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0524404040"));
                startActivity(appel);

            }
        });
    }

    public void initAddPart()
    {
        nomMonument = v.findViewById(R.id.nom);
        descMonument = v.findViewById(R.id.description);
        ajouterImage = v.findViewById(R.id.addImage);
        ajouter = v.findViewById(R.id.addEnt);
        image = v.findViewById(R.id.image);
        radios = v.findViewById(R.id.radioGroup);
        jour = v.findViewById(R.id.jour);
        nuit = v.findViewById(R.id.nuit);
        linearLayout = v.findViewById(R.id.linearlayoutFragHelp);
        confSucces=v.findViewById(R.id.txtConfirmation);
        delImages=v.findViewById(R.id.delImagesHelp);

        confSucces.setVisibility(View.INVISIBLE);

        ajouterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Perms", "onClick called");

                if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_GALLERY);
                    return;
                }


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);


                Log.d("Perms", "requesting perm done");

            }
        });

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nomMonument.getText().length()>0 && descMonument.getText().length()>0)
                {
                    RadioButton r = v.findViewById(radios.getCheckedRadioButtonId());
                    String type;
                    if(r==jour)
                    {
                        type = "jour";
                    }
                    else
                    {
                        type = "nuit";
                    }

                    if(imagesList.size()==0)
                    {
                        Resources res = getContext().getResources();
                        Drawable d = res.getDrawable(R.drawable.nophoto);
                        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        imagesList.add(stream.toByteArray());
                    }

                    PageFragment.getDb().insertData(nomMonument.getText().toString(),descMonument.getText().toString(), type, imagesList );

                    imagesList.clear();
                    confSucces.setVisibility(View.VISIBLE);

                    Resources res = getContext().getResources();
                    Drawable d = res.getDrawable(R.drawable.nophoto);
                    Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                    image.setImageBitmap(bitmap);

                }
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
                image.setImageBitmap(bitmap);

                linearLayout.addView(image);
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("Perms", "inside onRequestPermission");
        if(requestCode == REQUEST_CODE_GALLERY)
        {

            Log.d("Perms", "inside first if of onRequestiPermissionResult");
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                Log.d("Perms", "inside 2nd if of onRequestPermissionResult");

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }

            else
            {
                Toast.makeText(getContext(),"Vous ne pouvez pas acceder aux fichier", Toast.LENGTH_LONG).show();
            }

            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public byte[] compressByte(byte[] imgByte)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] imageNew = stream.toByteArray();

        return imageNew;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try
        {

            Log.d("Perms", "inside onActivityResult");
            if(requestCode==REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data!=null)
            {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();

                        InputStream iStream = getContext().getContentResolver().openInputStream(uri);
                        byte[] inputData = getBytes(iStream);

                        imagesList.add(compressByte(inputData));
                    }
                }
                else {
                    Resources res = getContext().getResources();
                    Drawable d = res.getDrawable(R.drawable.nophoto);
                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    imagesList.add(stream.toByteArray());
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

        Glide.with(getContext())
                .load(imagesList.get(0))
                .asBitmap()
                .fitCenter()
                .into(image);
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

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}

