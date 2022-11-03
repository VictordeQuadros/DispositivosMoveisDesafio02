package com.example.desafio02;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpAsyncTask extends AsyncTask<String,Void,String> {


    @Override
    protected String doInBackground(String... strings) {

        try{
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if (status == 200) {
                Log.d("STATUS", "PAGE FOUND");
                InputStream str = new BufferedInputStream(con.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(str));
                StringBuilder bl = new StringBuilder();
                String input;
                while((input = rd.readLine()) != null) {
                    bl.append(input);
                }
                con.disconnect();
                return  bl.toString();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
