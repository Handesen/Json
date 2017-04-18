package com.example.handesen.json;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.type;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.getdata);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // new BackgroundTask().execute();
                try {
                    new BackgroundTask().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }


    class BackgroundTask extends AsyncTask<Void,Void,String>{

        EditText et = (EditText) findViewById(R.id.editText);
        EditText et2 = (EditText) findViewById(R.id.editText2);
        String json_url,username,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json_url = "http://szakdolgozat.comxa.com/Login.php";
            username =  et.getText().toString();
            password = et2.getText().toString();

        }

        @Override
        protected String doInBackground(Void... params) {


            StringBuilder stringBuilder = new StringBuilder();
            try {

                StringBuilder tokenUri=new StringBuilder("username=");
                tokenUri.append(URLEncoder.encode(username,"UTF-8"));
                tokenUri.append("&password=");
                tokenUri.append(URLEncoder.encode(password,"UTF-8"));


                URL obj = new URL(json_url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");

                con.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
                outputStreamWriter.write(tokenUri.toString());
                outputStreamWriter.flush();

                int responseCode = con.getResponseCode();
                System.out.println("POST Response Code :: " + responseCode);


                // For POST only - END
                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    System.out.println(response.toString());
                    return response.toString().trim();

                } else {
                    System.out.println("POST request not worked");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String result) {
                valami(result);
            }
    }
    public void valami (String input){
        TextView textView = (TextView) findViewById(R.id.name);
        StringBuffer buff = new StringBuffer();
        try {
            JSONObject js = new JSONObject(input);
            JSONArray ja = js.getJSONArray("result");
            int count = 0;
            String username,email,name,created_at,remember_token;

            while(count<ja.length()){
                JSONObject jo = ja.getJSONObject(count);
              /*  username = jo.getString("username");
                email = jo.getString("email");
                name = jo.getString("name");
                created_at = jo.getString("created_at");
                remember_token = jo.getString("remember_token");
                buff.append(username + "/" + email + "/" + name + "/" + created_at + "/" + remember_token + "\n");*/
                boolean succes = jo.getBoolean("succes");
                if (succes){
                    textView.setText("SIKERES BELÉPÉS");
                }
                else{
                    textView.setText("sikertelen bejelentkezés");
                }
                count++;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
      //  textView.setText(buff.toString());

    }


}