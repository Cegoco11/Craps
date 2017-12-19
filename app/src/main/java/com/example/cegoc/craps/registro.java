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
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Yisusbot
 */
public class Registro extends AppCompatActivity {

    private EditText et1;
    private EditText et2;
    private EditText et3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        et1 = (EditText) findViewById(R.id.nombre);
        et2 = (EditText) findViewById(R.id.clave);
        et3 = (EditText) findViewById(R.id.correo);
    }

    public void GuardarUsuario (View view){

        String nombre = et1.getText().toString();
        String clave = et2.getText().toString();
        String correo = et3.getText().toString();

        File file = getFileStreamPath(nombre); //Esta funcion se usa para comprobar si existe ya un archivo creado en memoria

        Toast toast1 = Toast.makeText(getApplicationContext(), "Nuevo usuario guardado: "+ nombre, Toast.LENGTH_LONG);
        Toast toast2 = Toast.makeText(getApplicationContext(), "Nombre repetido o no valido", Toast.LENGTH_SHORT);

        Toast.makeText(this, "Hola", Toast.LENGTH_LONG);
        if (!file.exists()) {

            Jugador player = new Jugador(nombre, clave, correo);

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
                editor.putString("name", player.getNombre());

                editor.commit();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finishAffinity(); //Esto se usa para cerrar esta activity y todos sus padres, asi no nos quedan actividades colgadas por ahí
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            toast2.show();
        }
    }
}
