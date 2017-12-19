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


    String usuario_activo;
    private TextView tv1;
    private TextView tv2;
    private Jugador aux = new Jugador("Aux", "asd", "asdasdsad"); //Objeto de la clase Jugador donde cargaremos al jugador que este activo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);

        SharedPreferences prefe=getSharedPreferences("Active_User", Context.MODE_PRIVATE); //Usamos el sharedpreferences para saber que usuario esta activo
        usuario_activo = prefe.getString("name","Invitado"); //Si no existe uno por defecto es Invitado
        tv1.setText("Usuario: "+ usuario_activo);

        if (usuario_activo != "Invitado"){ //Si tenemos un jugador activo cargamos de la memoria el objeto que se corresponde a ese usuario

            try {

                FileInputStream fis = openFileInput(usuario_activo);
                ObjectInputStream in = new ObjectInputStream(fis);
                aux = (Jugador) in.readObject();
                in.close();
                tv2.setText("Monedas: "+ aux.getMonedas());


            } catch (Exception e) {
               e.printStackTrace();

            }

        }

        else{
            tv2.setText("Monedas: 5");
        }

    }


    public void Usuarios (View view){

        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }

    public void Jugar (View view){

        Intent intent = new Intent(this, CrapsPlay.class);
        startActivity(intent);
    }

    public void Cartas (View view){

        Intent intent = new Intent(this, Coleccion.class);
        startActivity(intent);

    }

    public void Salir (View view){
        SharedPreferences preferencias=getSharedPreferences("Active_User", Context.MODE_PRIVATE); //Salimos y borramos el sharedpreferences del usuario activo, al iniciar la app otra vez volvera a salir por defecto invitado
        SharedPreferences.Editor editor = preferencias.edit();
        editor.clear();
        editor.commit();
        this.finish();
    }
}
