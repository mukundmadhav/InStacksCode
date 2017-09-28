package com.mukundmadhav.instacks;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class MyAsyncTask extends AsyncTask<String,Void,String> {
    String line = null;
    String html = "";
    @Override
    protected String doInBackground(String... urls) {
        URL url;
        HttpURLConnection httpURLConnection;

        InputStream is;

        try {
            url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder str = new StringBuilder();
            line = null;
            while((line = reader.readLine()) != null){
                str.append(line);
            }
            is.close();
            html = str.toString();
            return html;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
