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
import android.widget.Toast;


public class tienda extends AppCompatActivity {

    private GridLayout avatares, skins;
    private int i;
    private Jugador prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Oculta Titulo de la ventanagetSupportActionBar().hide(); // Oculta Titulo de la ventana
        setContentView(R.layout.coleccion);

        // Usuario de prueba para comprobar que funciona correctamente
        prueba = new Jugador("Cesar", "asd", "asadsasad");
        // ToDo Leer fichero del usuario actual, para ello tenemos en el SharedPreferences su nombre
        // ToDo Leer el fichero y trabajar con el jugador leido
        // ToDo A la hora de comprar se tienen que cambiar los datos del fichero guardado

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
        for (i = 0; i < prueba.getAvatares().size(); i++) {
            ll_aux = new LinearLayout(this);
            ll_aux.setOrientation(LinearLayout.VERTICAL);
            ll_aux.setLayoutParams(params);
            if (prueba.getAvatares().get(i).getEstado()) {
                aux = new ImageView(this);
                aux.setImageResource(prueba.getAvatares().get(i).getImg());
                ll_aux.addView(aux);
                tv_aux=new TextView(this);
                tv_aux.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv_aux.setText(String.valueOf(prueba.getAvatares().get(i).getPrecio()));
                ll_aux.addView(tv_aux);
                ll_aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(tienda.this,R.string.Yatienes, Toast.LENGTH_SHORT).show();
                    }
                });
            } else{
                aux = new ImageView(this);
                aux.setImageResource(prueba.getAvatares().get(i).getImg_bloq());
                ll_aux.addView(aux);
                tv_aux=new TextView(this);
                tv_aux.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv_aux.setText(String.valueOf(prueba.getAvatares().get(i).getPrecio()));
                ll_aux.addView(tv_aux);
                ll_aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Comprar

                        Toast.makeText(tienda.this,R.string.WIP, Toast.LENGTH_SHORT).show();
                        prueba.getAvatares().get(i).compra(); // Solo cambia el estado no gasta monedas
                    }
                });
            }
            avatares.addView(ll_aux);
        }
        skins = (GridLayout) findViewById(R.id.SkinsGrid);
        // ToDo Mostrar skins
        for (i = 0; i < prueba.getDados().size(); i++) {
            ll_aux = new LinearLayout(this);
            ll_aux.setOrientation(LinearLayout.VERTICAL);
            ll_aux.setLayoutParams(params);
            if (prueba.getAvatares().get(i).getEstado()) {
                aux = new ImageView(this);
                aux.setImageResource(prueba.getDados().get(i).getImg());
                ll_aux.addView(aux);
                tv_aux=new TextView(this);
                tv_aux.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv_aux.setText(String.valueOf(prueba.getDados().get(i).getPrecio()));
                ll_aux.addView(tv_aux);
                ll_aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(tienda.this, R.string.Yatienes, Toast.LENGTH_SHORT).show();
                    }
                });
            } else{
                aux = new ImageView(this);
                aux.setImageResource(prueba.getDados().get(i).getImg_bloq());
                ll_aux.addView(aux);
                tv_aux=new TextView(this);
                tv_aux.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv_aux.setText(String.valueOf(prueba.getDados().get(i).getPrecio()));
                ll_aux.addView(tv_aux);
                ll_aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Comprar
                    }
                });
            }
            skins.addView(ll_aux);
        }
    }
}
