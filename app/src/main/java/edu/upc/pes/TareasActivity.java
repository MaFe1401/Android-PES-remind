package edu.upc.pes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class TareasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tareas);
        System.out.println("hola");
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {

                    String query = String.format("http://10.0.2.2:9000/Tareas/ListaTareasAndroid"+"?nombre="+Singleton.getInstance().username);
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    //OutputStream os = conn.getOutputStream();
                    /*String params = "nombre=" + Singleton.getInstance().username;

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params);*/
                    System.out.println("hola");
                    /*writer.flush();
                    writer.close();*/
                   // os.close();
                    stream = conn.getInputStream();
                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    Log.i("Result:lista tareas", result);
                    System.out.println(result);
                    boolean post = handler.post(new Runnable() {
                        public void run() {
                            if (result != null) {
                                ShowTable(result);
                            } else {
                                Toast.makeText(getApplicationContext(), "ALGO FALLÓ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void ShowTable(String result) {

        try {
            //JSONArray jArray = new JSONArray(result);
            Singleton.getInstance().listaTareas = new JSONArray(result);

            TableLayout tv = (TableLayout) findViewById(R.id.UserTable);
            tv.removeAllViews();
            System.out.println("hola");
            int flag = 1;

            // when i=-1, loop will display heading of each column
            // then usually data will be display from i=0 to jArray.length()
            //for(int i=-1;i<jArray.length();i++){
            for (int i = -1; i < Singleton.getInstance().listaTareas.length(); i++) {

                TableRow tr = new TableRow(TareasActivity.this);
                tr.setLayoutParams(new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                ));

                // this will be executed once
                if (flag == 1) {

                    TextView b3 = new TextView(TareasActivity.this);
                    b3.setText("DESCRIPCION");
                    b3.setTextColor(Color.BLUE);
                    b3.setTextSize(15);
                    tr.addView(b3);



                    TextView b5 = new TextView(TareasActivity.this);
                    b5.setPadding(20, 0, 0, 0);
                    b5.setText("FECHA");
                    b5.setTextColor(Color.BLUE);
                    b5.setTextSize(15);
                    tr.addView(b5);


                    TextView b7 = new TextView(TareasActivity.this);
                    b7.setPadding(40, 0, 0, 0);
                    b7.setText("LISTA");
                    b7.setTextColor(Color.BLUE);
                    b7.setTextSize(15);
                    tr.addView(b7);


                    TextView b9 = new TextView(TareasActivity.this);
                    b9.setPadding(60, 0, 0, 0);
                    b9.setText("ETIQUETA");
                    b9.setTextColor(Color.BLUE);
                    b9.setTextSize(15);
                    tr.addView(b9);
                    tv.addView(tr);

                    final View vline = new View(TareasActivity.this);
                    vline.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,2));
                    vline.setBackgroundColor(Color.BLUE);
                    tv.addView(vline); // add line below heading
                    flag = 0;
                } else {


                    //JSONObject json_data = jArray.getJSONObject(i);
                    JSONObject json_data = Singleton.getInstance().listaTareas.getJSONObject(i);

                    // afegim elements a la fila

                    TextView b = new TextView(TareasActivity.this);
                    //String str=String.valueOf(json_data.getInt("username"));
                    String str = json_data.getString("descripcion");
                    b.setText(str);
                    b.setTextColor(Color.RED);
                    b.setTextSize(15);
                    tr.addView(b);

                    TextView b2 = new TextView(TareasActivity.this);
                    b2.setPadding(20, 0, 0, 0);
                    //String str2=String.valueOf(json_data.getInt("email"));
                    String str2 = json_data.getString("fecha");
                    b2.setText(str2);
                    b2.setTextColor(Color.RED);
                    b2.setTextSize(15);
                    tr.addView(b2);

                    TextView b4 = new TextView(TareasActivity.this);
                    b4.setPadding(40, 0, 0, 0);
                    //String str2=String.valueOf(json_data.getInt("email"));
                    String str3 = json_data.getString("lista");
                    b4.setText(str3);
                    b4.setTextColor(Color.RED);
                    b4.setTextSize(15);
                    tr.addView(b4);

                    TextView b6 = new TextView(TareasActivity.this);
                    b6.setPadding(60, 0, 0, 0);
                    //String str2=String.valueOf(json_data.getInt("email"));
                    String str4 = json_data.getString("etiqueta");
                    b6.setText(str4);
                    b6.setTextColor(Color.RED);
                    b6.setTextSize(15);
                    tr.addView(b6);

                    // afegim la fila a la taula
                    tv.addView(tr);

                    final View vline1 = new View(TareasActivity.this);
                    vline1.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    vline1.setBackgroundColor(Color.BLACK);
                    tv.addView(vline1);  // add line below each row

                    tr.setId(i);


                }
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
            Toast.makeText(getApplicationContext(), "JsonArray fail", Toast.LENGTH_SHORT).show();
        }

    }
}

