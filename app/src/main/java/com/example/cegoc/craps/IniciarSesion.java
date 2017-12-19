package com.example.cegoc.craps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;

/**
 * Created by pablo on 15/12/2017.
 */

public class IniciarSesion extends AppCompatActivity {


    private EditText txtusuario;
    private EditText txtclave;
    private TextView tv1;
    private TextView tv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layaut_iniciarsesion);
        txtusuario = (EditText) findViewById(R.id.txtusuario);
        txtclave = (EditText) findViewById(R.id.txtclave);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);


    }


    public void Cargar(View view) {


        Jugador aux = new Jugador("Aux", "asd");


        String nombre = txtusuario.getText().toString();
        String contra = txtclave.getText().toString();
        File file = getFileStreamPath(nombre);
        Toast toast1 = Toast.makeText(getApplicationContext(), "No existe ese usuario", Toast.LENGTH_LONG);
        File file2 = getFileStreamPath(contra);
        Toast toast2 = Toast.makeText(getApplicationContext(), "No existe la contraseña", Toast.LENGTH_LONG);


        if (file.exists()&&file2.exists()) {
            FileInputStream fis;
            FileInputStream fis2;
            ObjectInputStream in = null;

            try {

                fis = openFileInput(nombre);
                fis2 = openFileInput(contra);
                in = new ObjectInputStream(fis);
                in = new ObjectInputStream(fis2);
                aux = (Jugador) in.readObject();
                in.close();

                SharedPreferences preferencias = getSharedPreferences("Active_User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("name", aux.mostrarnombre());
                editor.commit();


                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finishAffinity();


            } catch (Exception e) {
                toast1.show();
                e.printStackTrace();

            }

        } else {
            toast1.show();

        }

    }


}