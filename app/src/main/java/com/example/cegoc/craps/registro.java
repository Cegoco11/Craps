package com.example.cegoc.craps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Yisusbot
 */

public class registro extends AppCompatActivity {

    private EditText et1, et2, et3, et4;
    private boolean campo1, campo2, campo3, campo4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Oculta Titulo de la ventana
        setContentView(R.layout.activity_registro);

        campo1=false;
        campo2=false;
        campo3=false;
        campo4=false;

        et1 = (EditText) findViewById(R.id.nombre);
        et2 = (EditText) findViewById(R.id.clave);
        et3 = (EditText) findViewById(R.id.clave2);
        et4 = (EditText) findViewById(R.id.correo);

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et1.getText().toString().isEmpty()){
                    campo1=true;
                }
            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                campo2=contraSegura();
                campo3=compruebaContra();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                campo3=compruebaContra();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et4.getText().toString().isEmpty()){
                    campo4=true;
                }
            }
        });
    }

    public void GuardarUsuario (View view){
        if(campo1 && campo2 && campo3 && campo4){
            String nombre = et1.getText().toString();
            String clave = et2.getText().toString();
            String correo = et4.getText().toString();

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
                    editor.putInt("coins", player.getMonedas());
                    editor.putInt("skin", player.getDadosActual());
                    editor.putInt("avatar", player.getAvatarActual());
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
        } else{
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void irIniciarSesion(View v){
        Intent intent = new Intent(this, IniciarSesion.class);
        startActivity(intent);
    }


    /**
     * Comprueba que los valores de et2 y et3 sean iguales, y muestra una imagen u otra
     */
    private boolean compruebaContra(){
        String campo1=et2.getText().toString();
        String campo2=et3.getText().toString();
        ImageView imagen2=(ImageView) findViewById(R.id.img2);

        if(campo1.equals(campo2)){
            imagen2.setImageResource(R.drawable.logo_correcto);
            return true;
        } else{
            imagen2.setImageResource(R.drawable.logo_incorrecto);
            return false;
        }
    }

    /**
     * Comprueba que la contraseña sea segura (Longitud, mayusculas, numeros...)
     */
    private boolean contraSegura(){
        String campo1=et2.getText().toString();

        boolean esMayus=false, esNum=false;
        for(int i=0; i<campo1.length();i++){
            for(char j='A'; j<'Z';j++){
                if(campo1.charAt(i)==j){
                    esMayus=true;
                }
            }
            for(int f=0; f<10;f++){
                if(Character.getNumericValue(campo1.charAt(i))==f){
                    esNum=true;
                }
            }
        }
        // ToDo
        ImageView imagen1=(ImageView) findViewById(R.id.img1);
        if(campo1.length()>=6 && esMayus && esNum){
            imagen1.setImageResource(R.drawable.logo_correcto);
            return true;
        } else{
            imagen1.setImageResource(R.drawable.logo_incorrecto);
            return false;
        }
    }

}
