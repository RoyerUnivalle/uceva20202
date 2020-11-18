package com.example.uceva20202.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.example.uceva20202.Home;

public class MyService extends Service {

    Notificar obj;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        obj = new Notificar();
        obj.execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class Notificar extends AsyncTask<Void, Integer,Void> { // parametros,progreso, resultado
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
            for (int j=0; j<= 10; j++){
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
            Toast.makeText(getApplicationContext(), "iterador: "+values[0], Toast.LENGTH_LONG).show();
        }
    }
}
