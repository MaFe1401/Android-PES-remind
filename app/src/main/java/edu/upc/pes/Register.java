package edu.upc.pes;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("REGISTRO");
    }
    public void registro(View view) {
        EditText nombre=(EditText) findViewById(R.id.name);
        EditText contra=(EditText) findViewById(R.id.password);
        if((TextUtils.isEmpty(nombre.getText().toString())) || (TextUtils.isEmpty(contra.getText().toString()))){
            Toast.makeText(getApplicationContext(), "¡Rellena los campos!",Toast.LENGTH_LONG).show();
        }
        else {
            new Register.RegisterMessage(this).execute("http://10.0.2.2:9000/Application/signUp");
        }
    }

    private class RegisterMessage extends AsyncTask<String, Void, String> {
        Context context;
        InputStream stream = null;
        String str = "";
        String result = null;

        private RegisterMessage(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... urls) {


            try {
                EditText nombre=(EditText) findViewById(R.id.name);
                EditText contra=(EditText) findViewById(R.id.password);
                String query = String.format(urls[0]);
                URL url = new URL(query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();
                String params = "nombre=" + nombre.getText().toString() + "&" + "contra=" + contra.getText().toString();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(params);

                writer.flush();
                writer.close();
                os.close();
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
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Correcto")){
                Toast.makeText(getApplicationContext(), "OK",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "ALGO FALLÓ",Toast.LENGTH_LONG).show();
            }

        }
    }
}
