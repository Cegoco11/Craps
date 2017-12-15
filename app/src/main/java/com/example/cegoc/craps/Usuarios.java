package com.example.cegoc.craps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Usuarios extends AppCompatActivity {



    private EditText et1;
    private ArrayList<String> myList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_usuarios);

        et1 = (EditText) findViewById(R.id.input_usuario);

    }


    public void Guardar (View view){

        String nombre = et1.getText().toString();
        String contra = et1.getText().toString(); // Prueba
        File file = getFileStreamPath(nombre); //Esta funcion se usa para comprobar si existe ya un archivo creado en memoria



        Toast toast1 = Toast.makeText(getApplicationContext(), "Nuevo usuario guardado: "+ nombre, Toast.LENGTH_LONG);
        Toast toast2 = Toast.makeText(getApplicationContext(), "Nombre repetido o no valido", Toast.LENGTH_LONG);



        if (!file.exists()) {

            Jugador player = new Jugador(nombre, contra);

            FileOutputStream fos;
            ObjectOutputStream out = null;

            try {

                fos = openFileOutput(nombre, Context.MODE_PRIVATE); //Guardamos cada objeto de la clase Jugador en un archivo en memoria que lleve por nombre el nombre del jugador
                out = new ObjectOutputStream(fos);
                out.writeObject(player);
                out.close();
                toast1.show();
                SharedPreferences preferencias=getSharedPreferences("Active_User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("name", player.mostrarnombre());

                editor.commit();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finishAffinity(); //Esto se usa para cerrar esta activity y todos sus padres, asi no nos quedan actividades colgadas por ahí

            } catch (Exception e) {

                toast2.show();
                e.printStackTrace();
            }
        }

        else{
            toast2.show();
        }



    }

    public void Cargar (View view) {


        Jugador aux = new Jugador("Aux", "asd");



        String nombre = et1.getText().toString();
        File file = getFileStreamPath(nombre);
        Toast toast1 = Toast.makeText(getApplicationContext(), "No existe ese usuario", Toast.LENGTH_LONG);


        if (file.exists()) {
            FileInputStream fis;
            ObjectInputStream in = null;

            try {

                fis = openFileInput(nombre);
                in = new ObjectInputStream(fis);
                aux = (Jugador) in.readObject();
                in.close();

                SharedPreferences preferencias=getSharedPreferences("Active_User", Context.MODE_PRIVATE);
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

        }

        else{
            toast1.show();

        }

    }

    public void Borrar (View view){

        String nombre = et1.getText().toString();
        File file = getFileStreamPath(nombre);
        Toast toast1 = Toast.makeText(getApplicationContext(), "Usuario: " + nombre + " ha sido eliminado", Toast.LENGTH_LONG);
        Toast toast2 = Toast.makeText(getApplicationContext(), "No existe ese usuario", Toast.LENGTH_LONG);


        if (file.exists()) {

            File dir = getFilesDir();
            File file2 = new File(dir, nombre);
            boolean deleted = file.delete();
            toast1.show();
            SharedPreferences preferencias=getSharedPreferences("Active_User", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("name", "Invitado");
            editor.commit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }

        else{
            toast2.show();
        }



    }




    }
