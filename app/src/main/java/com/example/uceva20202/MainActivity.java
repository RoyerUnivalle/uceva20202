package com.example.uceva20202;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etName, etPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Cristian lopez
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"Hola Oncreate", Toast.LENGTH_LONG).show();
        /*
        R: MATRIZ DE RECURSOS. SI HAY UN ERROR EN LA UI (XML) R NO SE RECONOCERA
        * */
        etName = findViewById(R.id.etName);
        etPasswd = findViewById(R.id.etPasswd);
    }

    public void navegar(View v){
        Intent ir = new Intent(this,Home.class);
        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TASK | ir.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle data = new Bundle();
        // validar que los datos se esten tipiando
        data.putString("name",etName.getText().toString());
        data.putString("passwd",etPasswd.getText().toString());
        ir.putExtras(data);
        startActivity(ir);
    }

    @Override // Cristian T
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override // Cristian T
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onStart() { // Arlex
        super.onStart();
        Toast.makeText(this,"Hola onStart", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStop() { // Alexis
        super.onStop();
        Toast.makeText(this,"Hola onStop", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() { // Cristian R
        super.onDestroy();
        Toast.makeText(this,"Hola onDestroy", Toast.LENGTH_LONG).show();
    }
}
