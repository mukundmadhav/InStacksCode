package com.mukundmadhav.instacks;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.dd.CircularProgressButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Home extends Fragment {

    String Code = "";
    String fullProfilePicURL = "";
    static String spp = "";
    String parts2[];

    public void imageDownload(Context ctx, String url){

        Target targetn= getTarget(url);
        Picasso.with(ctx)
                .load(spp)
                .into(targetn);
        circularProgressButton.setProgress(100);
        File file1 = new File(Environment.getExternalStorageDirectory().getPath() +"/InStacks" + "/" + url);
        new SingleMediaScanner(getActivity(),file1);
    }

    CircularProgressButton circularProgressButton;

    //Using the Picasso Target Class

    private Target getTarget(String url){
        final String temp = url;
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/InStacks" + "/" + temp);
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    public String fetchProfileURL (String sourceCode) {
         fullProfilePicURL = "";
        String ppURL = "";
        Pattern p = Pattern.compile("\"profile_pic_url_hd\": \"(.*?)\"");
        Matcher m = p.matcher(sourceCode);
        if (m.find()) {
            String parts[];
            Log.i("Found value: " , m.group(1));
            ppURL = m.group(1);
            if (ppURL.contains("/s320x320")) {
                parts = ppURL.split("/s320x320");
            } else {
                parts = ppURL.split("/s150x150");
            }

            if (parts[0] != null && parts[1] != null) {

                fullProfilePicURL = parts[0] + parts[1];
                Log.i("Parts:",fullProfilePicURL);
                spp = fullProfilePicURL;
                imageDownload(getActivity(),parts2[1]+".jpg");
                Log.i("Into?","Yes");
                Log.i("Going?","spp");


            }
        }

        return fullProfilePicURL;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.home_layout,container,false);

        final String inputaURL = " ";


        final EditText urlSPace = (EditText) v.findViewById(R.id.edit_URL);

        //https://www.instagram.com/casianandews



        Button GoButton = (Button) v.findViewById(R.id.GoButton);
        circularProgressButton = (CircularProgressButton) v.findViewById(R.id.btnWithText);
        Button ReButton = (Button) v.findViewById(R.id.ResetButton);
         final TextView textView = (TextView) v.findViewById(R.id.textView);
        ReButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circularProgressButton.setProgress(100);
            }
        });

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circularProgressButton.setProgress(30);
            }
        });

        GoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String inputURL = inputaURL;
                textView.setText("Loading");
                circularProgressButton.setProgress(1);
                String inputURL = urlSPace.getText().toString();
                parts2 = inputURL.split(".com/");
                Log.i("Part1:",parts2[0]);
                Log.i("Part2 :",parts2[1]);
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                try {
                    Code = myAsyncTask.execute(inputURL).get();
                    Log.i("COde: ", Code);
                    Log.i("Profile Pic : ", fetchProfileURL(Code));
                    circularProgressButton.setProgress(50);
                    textView.setVisibility(View.INVISIBLE);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });




        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home - InStacks");
    }
}
