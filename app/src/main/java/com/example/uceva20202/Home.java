package com.example.uceva20202;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uceva20202.services.MyService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Home extends AppCompatActivity implements View.OnClickListener {

    Button btnInterface, btnDelegado, btnContar, btnUrl;
    EditText et1;
    TextView data;
    public String username = "";
    public String passwd = "";
    public int contador=0;
    Button btnColorear;
    Colorear obj; // objeto
    ConsultaHttp obj2; // objeto
    // Instantiate the RequestQueue.
    RequestQueue queue;
    String url ="https://www.google.com";
    public int cantidadRegistros;
    public JSONArray dataAgenda;

    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // recovering the instance state
        if (savedInstanceState != null) {
            contador = savedInstanceState.getInt("contador");
        }

        //Enlace buttons
        btnInterface = findViewById(R.id.button2);
        btnDelegado = findViewById(R.id.button3);
        btnContar = findViewById(R.id.button4);
        btnColorear = findViewById(R.id.btnColorear);
        btnUrl = findViewById(R.id.bntHttpurl);

        //Enlace EditTex
        et1 = findViewById(R.id.editText3);

        data = findViewById(R.id.tvData);

        username = getIntent().getExtras().getString("name");
        passwd = getIntent().getExtras().getString("passwd");
        data.setText("Username: "+username+" Paswd:"+ passwd);

        // Forma de añadir evento a un boton - DELEGADO
        btnDelegado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hola método delegado", Toast.LENGTH_LONG).show();
            }
        });

        // Forma de añadir evento a un boton - INTERFACE
        btnInterface.setOnClickListener(this);
        btnContar.setOnClickListener(this);
        btnUrl.setOnClickListener(this);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("agenda");
        myRef.setValue("Hello, World!");
        this.readFirebase();

        // Read from the database
        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                System.out.println("Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });*/
    }

    public void readFirebase(){
        System.out.println(myRef.child("agenda"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "Hola método onStart", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "Hola método onStop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Hola método onDestroy", Toast.LENGTH_LONG).show();
    }

    public void volver(View g){
        Intent volver = new Intent(this,MainActivity.class);
        volver.addFlags(volver.FLAG_ACTIVITY_CLEAR_TASK | volver.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(volver);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("contador",contador);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void inicarServicio(View k){
        Intent servicio = new Intent(this, MyService.class);
        startService(servicio);
    }

    public void contar(){
        contador++;
        System.out.println("contador: " + contador);
        et1.setText(""+contador);
        myRef.child("contador").setValue(contador);
    }

    public int rangoColor(){
        return (int) (Math.random()*255 + 1);
    }

    public void colorear(View f){ // En el mismo hilo que administra la UI
        for (int i =0; i<= contador; i++){
            try {
                btnColorear.setBackgroundColor(Color.rgb(this.rangoColor(),this.rangoColor(),this.rangoColor()));
                Thread.sleep(1000); // HILO DE LA UI - MAIN THREAD
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void colorear3(View f){ // Forma 2: Por medio de *AsyntTask* (clase)
        obj = new Colorear();
        obj.execute(); // no paso ningún paramétro porque AsyntTask tiene VOID en parametros
    }

    public void colorear2(View f){ // Forma1 de ejecutar en un hilo independiente
        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                for (int i =0; i<= contador; i++){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    btnColorear.setBackgroundColor(Color.rgb(this.rangoColor(),this.rangoColor(),this.rangoColor()));
                }
            }
            public int rangoColor(){
                return (int) (Math.random()*255 + 1);
            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:
                Toast.makeText(this, "Hola interface", Toast.LENGTH_LONG).show();
                break;
            case  R.id.button4:
                this.contar();
                break;
            case  R.id.bntHttpurl:
                try {
                    this.httpUrl();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default: break;
        }
    }


    public class Colorear extends AsyncTask<Void, Integer,Void>{ // parametros,progreso, resultado
        Home objt2;
        public Colorear(){
            objt2 = new Home();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Void aVoid) { // resultado
            super.onPostExecute(aVoid);
        }
        @Override // No tengo acceso a la UI (button, conbobox, WIDGET)
        protected Void doInBackground(Void... voids) { // se hace en hilos diferentes al hilo de la UI
            for (int j=0; j<= contador; j++){
                publishProgress(j); //-> esto llama al método onProgressUpdate (ojo con los parametros)
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {/// Si tiene acceso a la UI
            super.onProgressUpdate(values);
            btnColorear.setBackgroundColor(Color.rgb(objt2.rangoColor(),objt2.rangoColor(),objt2.rangoColor()));
        }
    }

    public void startVolley(View l){
        // Request a string response from the provided URL.
        this.url = "https://invessoft.com/api/eventos/2";
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            VolleyLog.v("Response:%n %s",response.getInt("count"));
                            cantidadRegistros=response.getInt("count");
                            dataAgenda = response.getJSONArray("agenda"); // for foreach
                            System.out.println("cantidadRegistros1: "+cantidadRegistros);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        queue.add(req);

    }

    public class ConsultaHttp extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("https://invessoft.com/api/eventos/2");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null; // abrimos conexion
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.HTTP_OK == urlConnection.getResponseCode()){
                    System.out.println("conectado");
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bR = new BufferedReader(  new InputStreamReader(inputStream));
                    String line = "";

                    StringBuilder responseStrBuilder = new StringBuilder();
                    while((line =  bR.readLine()) != null){

                        responseStrBuilder.append(line);
                    }
                    inputStream.close();
                    JSONObject result= new JSONObject(responseStrBuilder.toString());
                    cantidadRegistros =result.getInt("count");
                    System.out.println("cantidadRegistros2: "+ result.get("count"));
                    //urlConnection.getInputStream();
                }else {
                    System.out.println("error");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            finally {
                System.out.println("disconnect");
                urlConnection.disconnect();
            }
            return null;
        }
    }

    public void httpUrl() throws IOException {
        obj2 = new ConsultaHttp();
        obj2.execute();
    }
}
