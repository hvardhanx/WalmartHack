package com.example.android.hospice.HomePage;

import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hospice.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MediStore extends android.support.v4.app.Fragment {
    public String test;
    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 2;
    public long nextID;
    private static int LENGTH_M = 5;
    private static Bitmap bitmap = null;
    private Button uploadButton;
    private Button reOrder;
    private Button solo;


    public MediStore() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medicine_order, container, false);
        nextID = 1;
        uploadButton = (Button) view.findViewById(R.id.uploadButton);
        reOrder = (Button) view.findViewById(R.id.reOrderButton);
        solo = (Button) view.findViewById(R.id.soloMedicines);
        solo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Upload individual image!!", Toast.LENGTH_SHORT).show();
            }
        });
        reOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CartDetails.class);
                startActivity(intent);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                        v.getContext());
                myAlertDialog.setTitle("Upload Pictures Option");
                myAlertDialog.setMessage("How do you want to set your picture?");

                myAlertDialog.setPositiveButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent pictureActionIntent = null;

                                pictureActionIntent = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                getActivity().startActivityForResult(pictureActionIntent,GALLERY_PICTURE);

                            }
                        });

                myAlertDialog.setNegativeButton("Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                File f = new File(android.os.Environment
                                        .getExternalStorageDirectory(), "temp.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(f));

                                getActivity().startActivityForResult(intent,
                                        CAMERA_REQUEST);

                            }
                        });
                myAlertDialog.show();
            }
        });

        return view;
    }

    private void saveToInternalStorage(Bitmap bmp, String filename) {
        ContextWrapper contextWrapper = new ContextWrapper(getActivity().getApplicationContext());
        File appDirectory = contextWrapper.getFilesDir();

        File currentPath = new File(appDirectory, filename);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(currentPath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 &&
                resultCode == 1 && null != data) {
            Toast.makeText(getActivity(), "Uploading...", Toast.LENGTH_LONG).show();
            Uri selectedImageUri = data.getData();
            try {
                Log.v("Path:", selectedImageUri.toString());
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                Log.d("Sam: ", String.valueOf(bitmap));

                Log.v("TAG NOTE:", "Product before created, ID: " + nextID);

                String filename = Long.toString(nextID);
                Log.v("Image path: ", filename);

                saveToInternalStorage(bitmap, filename);
                Intent intent = new Intent(getActivity(),CartDetails.class);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static Bitmap getBitmap(){
        return bitmap;
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
