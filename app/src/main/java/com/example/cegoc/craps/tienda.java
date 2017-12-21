package com.example.cegoc.craps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class tienda extends AppCompatActivity {

    private GridLayout avatares, skins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Oculta Titulo de la ventanagetSupportActionBar().hide(); // Oculta Titulo de la ventana
        setContentView(R.layout.coleccion);

        // Usuario de prueba para comprobar que funciona correctamente
        // Aqui habria que tener en cuenta quien es el usuario actual
        Jugador prueba = new Jugador("Cesar", "asd", "asadsasad");

        LinearLayout ll_aux;
        ImageView aux;
        TextView tv_aux;
        // Configuracion del LinearLayout
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        avatares = (GridLayout) findViewById(R.id.AvataresGrid);
        // Muestro todos los avatares, hay que filtrar por los que estan desbloqueados
        // Se crea un Linear layout verticaal, al que se le mete una imagen y un texto
        // Y este linear se añade a el Grid avatares
        for (int i = 0; i < prueba.getAvatares().size(); i++) {
            ll_aux = new LinearLayout(this);
            ll_aux.setOrientation(LinearLayout.VERTICAL);
            ll_aux.setLayoutParams(params);
            if (!prueba.getAvatares().get(i).getEstado()) {
                aux = new ImageView(this);
                aux.setImageResource(prueba.getAvatares().get(i).getImg());
                ll_aux.addView(aux);
                tv_aux=new TextView(this);
                tv_aux.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv_aux.setText(String.valueOf(prueba.getAvatares().get(i).getPrecio()));
                ll_aux.addView(tv_aux);
            }
            avatares.addView(ll_aux);
        }
        skins = (GridLayout) findViewById(R.id.SkinsGrid);
        // ToDo Mostrar skins
        for (int i = 0; i < prueba.getAvatares().size(); i++) {
            ll_aux = new LinearLayout(this);
            ll_aux.setOrientation(LinearLayout.VERTICAL);
            ll_aux.setLayoutParams(params);
            if (!prueba.getAvatares().get(i).getEstado()) {
                aux = new ImageView(this);
                aux.setImageResource(prueba.getDados().get(i).getImg());
                ll_aux.addView(aux);
                tv_aux=new TextView(this);
                tv_aux.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv_aux.setText(String.valueOf(prueba.getDados().get(i).getPrecio()));
                ll_aux.addView(tv_aux);
            }
            skins.addView(ll_aux);
        }
    }
}
