package com.example.cegoc.craps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class MainActivity extends AppCompatActivity {


    private String usuario_activo;
    private TextView tv1;
    private TextView tv2;
    private Jugador aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); // Oculta Titulo de la ventana

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);

        SharedPreferences prefe=getSharedPreferences("Active_User", Context.MODE_PRIVATE); //Usamos el sharedpreferences para saber que usuario esta activo
        SharedPreferences.Editor editor = prefe.edit();
        usuario_activo = prefe.getString("name","Invitado"); //Si no existe uno por defecto es Invitado

        if (usuario_activo != "Invitado"){ //Si tenemos un jugador activo cargamos de la memoria el objeto que se corresponde a ese usuario
            try {
                FileInputStream fis = openFileInput(usuario_activo);
                ObjectInputStream in = new ObjectInputStream(fis);
                aux = (Jugador) in.readObject();
                in.close();

                editor.putString("name", aux.getNombre());
                editor.putInt("coins", aux.getMonedas());
                editor.putInt("skin", aux.getDadosActual());
                editor.putInt("avatar", aux.getAvatarActual());
                editor.commit();

                tv1.setText(aux.getNombre());
                tv2.setText(String.valueOf(prefe.getInt("coins", 0)));
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        else{
            aux = new Jugador("Guest", "123456A", "correo@correo.com"); // Invitado
            editor.putString("name", aux.getNombre());
            editor.putInt("coins", 50);
            editor.putInt("skin", aux.getDadosActual());
            editor.putInt("avatar", aux.getAvatarActual());
            editor.commit();
            tv1.setText(prefe.getString("name",""));
            tv2.setText(String.valueOf(prefe.getInt("coins", 0)));
        }
    }

    public void Tienda (View view){
        Intent intent = new Intent(this, tienda.class);
        startActivity(intent);
    }

    public void Jugar (View view){
        Intent intent = new Intent(this, crapsPlay.class);
        startActivity(intent);
    }

    public void Cartas (View view){
        Intent intent = new Intent(this, Coleccion.class);
        startActivity(intent);
    }

    public void Salir (View view){
        this.finish();
    }

    public void Borrar (View v) {
        SharedPreferences preferencias=getSharedPreferences("Active_User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        // Leer fichero del usuario y guardar todos los datos
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, IniciarSesion.class);
        startActivity(intent);
        this.finish();
    }
}
