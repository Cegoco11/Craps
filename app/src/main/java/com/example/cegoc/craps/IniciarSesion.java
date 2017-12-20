package com.example.cegoc.craps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * @author Pablo
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

        Jugador aux = new Jugador("Aux", "asd", "asdfa");

        String nombre = txtusuario.getText().toString();
        String contra = Jugador.getMD5(txtclave.getText().toString());

        File file = getFileStreamPath(nombre);
        Toast toast = Toast.makeText(getApplicationContext(), "No existe ese usuario", Toast.LENGTH_LONG);

        if (file.exists()) {
            FileInputStream fis;
            ObjectInputStream in = null;
            try {

                fis = openFileInput(nombre);
                in = new ObjectInputStream(fis);
                aux = (Jugador) in.readObject();
                in.close();

                if (aux.getClave().equals(contra)) {
                    SharedPreferences preferencias = getSharedPreferences("Active_User", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencias.edit();
                    editor.putString("name", aux.getNombre());
                    editor.commit();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(this, "contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                toast.show();
                e.printStackTrace();
            }
        } else {
            toast.show();
        }
    }

    public void irRegistro(View v) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}