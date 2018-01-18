package com.mukundmadhav.instacks;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Home extends Fragment {


    String Code = "";
    String fullProfilePicURL = "";
    static String spp = "";
    ProgressDialog mProgressDialog;
    String parts2[];
    View v;

    

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/InStacks");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        new SingleMediaScanner(getActivity(),file);
        Toast.makeText(getActivity(), " Image Saved to InStacks", Toast.LENGTH_LONG).show();
    }

    private class DownloadImage extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Downloading Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageURL = strings[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            SaveImage(bitmap);
            mProgressDialog.dismiss();

            ImageView imageView = (ImageView) v.findViewById(R.id.imageView4);
            imageView.setImageBitmap(bitmap);

        }
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
                new DownloadImage().execute(spp);
                Log.i("Into?","Yes");
                Log.i("Going?","spp");


            }
        }

        return fullProfilePicURL;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.home_layout,container,false);



        final String inputaURL = " ";




        final EditText urlSPace = (EditText) v.findViewById(R.id.edit_URL);

        Button GoButton = (Button) v.findViewById(R.id.GoButton);
        Button ReButton = (Button) v.findViewById(R.id.ResetButton);
        final TextView textView = (TextView) v.findViewById(R.id.textView);
        ReButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlSPace.setText("");
            }
        });

        final View vz = v;

        String url = "https://instagram.com/casianandews/";


        GoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = urlSPace.getText().toString();
                String usernameURL = "https://www.instagram.com/"+username;


                if(username.matches("")) {
                    final Snackbar snakbar = Snackbar.make(vz,"Please Enter Something.", Snackbar.LENGTH_SHORT);
                    snakbar.show();

                }

                else {


                    if (MainActivity.isConnectedToNet == true) {
                        //String inputURL = inputaURL;

                        String inputURL = usernameURL;
                        try {
                            parts2 = inputURL.split(".com/");
                            Log.i("Part1:", parts2[0]);
                            Log.i("Part2 :", parts2[1]);
                        }
                        catch(Exception e) {
                            final Snackbar sbar = Snackbar.make(vz,"Invalid URL.", Snackbar.LENGTH_SHORT);
                            sbar.show();

                        }
                        MyAsyncTask myAsyncTask = new MyAsyncTask();
                        try {
                            Code = myAsyncTask.execute(inputURL).get();
                            Log.i("Profile Pic : ", fetchProfileURL(Code));


                        } catch (Exception e) {
                            e.printStackTrace();
                            final Snackbar sbar = Snackbar.make(vz,"Invalid Username.", Snackbar.LENGTH_LONG);
                            sbar.show();
                        }

                    } else {
                        final Snackbar snakbar = Snackbar.make(vz, "No Internet Connection! Please connect and restart the app.", Snackbar.LENGTH_INDEFINITE);
                        snakbar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snakbar.dismiss();
                            }
                        });
                        snakbar.show();
                    }
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
