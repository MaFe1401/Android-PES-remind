package edu.upc.pes;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private ProgressBar pb_circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("REMIND: INICIO");
        pb_circular = findViewById(R.id.progressBar2);
        pb_circular.setVisibility(View.INVISIBLE);
    }
    public void registerClick(View view){
        Intent intent = new Intent(this ,Register.class);
        startActivity(intent);
    }
    public void sayHelloThreads(View view) {
        pb_circular.setVisibility(View.VISIBLE);
        final EditText nombre = (EditText) findViewById(R.id.nombreText);
        final EditText contra = (EditText) findViewById(R.id.contraText);
        if ((TextUtils.isEmpty(nombre.getText().toString())) || (TextUtils.isEmpty(contra.getText().toString()))) {
            Toast.makeText(getApplicationContext(), "¡Rellena los campos!", Toast.LENGTH_LONG).show();
            pb_circular.setVisibility(View.INVISIBLE);
        } else {
            new Thread(new Runnable() {
                InputStream stream = null;
                String str = "";
                String result = null;
                Handler handler = new Handler();

                public void run() {

                    try {

                        String query = String.format("http://10.0.2.2:9000/Application/logInAndroid?nombre=" + nombre.getText().toString() + "&" + "contra=" + contra.getText().toString());
                        URL url = new URL(query);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.connect();


                        /*String params = "nombre=" + nombre.getText().toString() + "&" + "contra=" + contra.getText().toString();
                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(params);

                        writer.flush();
                        writer.close();
                        os.close();*/

                        stream = conn.getInputStream();

                        BufferedReader reader = null;

                        StringBuilder sb = new StringBuilder();

                        reader = new BufferedReader(new InputStreamReader(stream));

                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        result = sb.toString();

                        handler.post(new Runnable() {
                            public void run() {

                                if (result.equals("Correcto")) {
                                    Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                                    Singleton.getInstance().username=nombre.getText().toString();
                                    pb_circular.setVisibility(View.INVISIBLE);
                                    Context context = getApplicationContext();
                                    Intent intent = new Intent(context, TareasActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "ALGO FALLÓ", Toast.LENGTH_LONG).show();
                                    pb_circular.setVisibility(View.INVISIBLE);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
   /* public void sayHelloAsynktask(View view) {
        EditText nombre=(EditText) findViewById(R.id.nombreText);
        EditText contra=(EditText) findViewById(R.id.contraText);
        if((TextUtils.isEmpty(nombre.getText().toString())) || (TextUtils.isEmpty(contra.getText().toString()))){
            Toast.makeText(getApplicationContext(), "¡Rellena los campos!",Toast.LENGTH_LONG).show();
        }
        else {
            new HelloMessage(this).execute("http://10.0.2.2:9000/Application/logInAndroid?nombre=" + nombre.getText().toString() + "&" + "contra=" + contra.getText().toString());
        }
    }*/

    /*private class HelloMessage extends AsyncTask<String, Void, String> {
        Context context;
        InputStream stream = null;
        String str = "";
        String result = null;

        private HelloMessage(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... urls) {

            try {
                String query = String.format(urls[0]);
                URL url = new URL(query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 );
                conn.setConnectTimeout(15000 );
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                stream = conn.getInputStream();

                BufferedReader reader = null;

                StringBuilder sb = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();

                return result;

            } catch(IOException e) {
                return null;
            }
        }*/

      /*  @Override
        protected void onPostExecute(String result) {

            TextView respuesta = (TextView) findViewById (R.id.respuestaText);
            respuesta.setText("AsynkTask: " +result);
            if(result.equals("Correcto")){
                Toast.makeText(getApplicationContext(), "OK",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "ALGO FALLÓ",Toast.LENGTH_LONG).show();
            }

        }
    }*/
}
