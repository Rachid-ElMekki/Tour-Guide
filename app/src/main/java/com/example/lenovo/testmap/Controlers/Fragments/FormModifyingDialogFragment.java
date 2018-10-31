package com.example.lenovo.testmap.Controlers.Fragments;



import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.testmap.Adapters.BestOfAdapter;
import com.example.lenovo.testmap.Controlers.Dialogs.FormModifyingDialog;
import com.example.lenovo.testmap.Models.Data.Monuments;
import com.example.lenovo.testmap.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class FormModifyingDialogFragment extends DialogFragment
{

    public final static int REQUEST_CODE_GALLERY = 999;

    private FormModifyingDialog dialog;
    private BestOfAdapter boa;
    private Monuments monument;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
         dialog = new FormModifyingDialog(boa, this.getContext(), monument);

        dialog.getModifyImageButton().setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
                    return;
                }

                Log.d("Preparing", "Preparing to add!");

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Log.d("Preparing", "sending to onActivityResult!");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);

            }
        });

        return dialog;
    }

    public void initParams(BestOfAdapter boa, Monuments monument)
    {
        this.boa=boa;
        this.monument=monument;
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        dialog.setImagesList(new ArrayList<byte[]>());
        List<byte[]> tmpList=new ArrayList<byte[]>();
        try
        {

            Log.d("Perms", "inside onActivityResult");
            if(requestCode==REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data!=null)
            {
                Log.d("Addings", "Add !");
                ClipData clipData = data.getClipData();
                if (clipData != null)
                {
                    for (int i = 0; i < clipData.getItemCount(); i++)
                    {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();

                        InputStream iStream = getContext().getContentResolver().openInputStream(uri);
                        byte[] inputData = getBytes(iStream);

                        tmpList.add(inputData);
                        Log.d("Addings", "Added i="+i);
                    }
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

        Log.d("Addings", "Displaying");
        dialog.setImagesList(tmpList);

        for(int i=0; i<dialog.getImagesList().size(); i++)
        {
            ImageView imgView = new ImageView(getContext());
            Glide.with(getContext())
                    .load(dialog.getImagesList().get(i))
                    .asBitmap()
                    .fitCenter()
                    .into(imgView);
            dialog.getLinearLayout().addView(imgView);
        }

        Log.d("Addings", "Displayed !!!");

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
