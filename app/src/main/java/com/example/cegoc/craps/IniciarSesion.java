package com.example.cegoc.craps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        getSupportActionBar().hide(); // Oculta Titulo de la ventana
        setContentView(R.layout.layaut_iniciarsesion);

        final Typeface Pixel1=Typeface.createFromAsset(getAssets(), "Pixel1.ttf");

        txtusuario = (EditText) findViewById(R.id.txtusuario);
        txtusuario.setTypeface(Pixel1);
        txtclave = (EditText) findViewById(R.id.txtclave);
        txtclave.setTypeface(Pixel1);

        TextView titleLogin=(TextView)findViewById(R.id.titleLogin);
        titleLogin.setTypeface(Pixel1);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setTypeface(Pixel1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setTypeface(Pixel1);

        Button botonIniciar=(Button)findViewById(R.id.ingresar);
        botonIniciar.setTypeface(Pixel1);
        Button botonRegistro=(Button)findViewById(R.id.crearCuenta);
        botonRegistro.setTypeface(Pixel1);
    }

    public void Cargar(View view) {

        Jugador aux = new Jugador("Guest", "123456A", "correo@correo.com"); // Invitado

        String nombre = txtusuario.getText().toString();
        String contra = Jugador.getMD5(txtclave.getText().toString());

        File file = getFileStreamPath(nombre);
        Toast toast = Toast.makeText(getApplicationContext(),R.string.NoExis, Toast.LENGTH_LONG);

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
                    editor.putInt("coins", aux.getMonedas());
                    editor.putInt("skin", aux.getDadosActual());
                    editor.putInt("avatar", aux.getAvatarActual());
                    editor.commit();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(this,R.string.NoExis, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, registro.class);
        startActivity(intent);
    }
}