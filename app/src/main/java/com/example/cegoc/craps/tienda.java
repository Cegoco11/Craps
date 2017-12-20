package com.example.cegoc.craps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.widget.ImageView;


public class tienda extends AppCompatActivity {

    private GridLayout avatares, skins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tienda);

        Jugador prueba=new Jugador("Cesar","asd", "asadsasad");

        avatares=(GridLayout) findViewById(R.id.AvataresGrid);
        ImageView aux;

        for(int i=0; i<prueba.getAvatares().size(); i++){
            aux=new ImageView(this);
            aux.setImageResource(prueba.getAvatares().get(i).getImg());
            avatares.addView(aux);
        }
        skins=(GridLayout) findViewById(R.id.SkinsGrid);
        ImageView aux;

        for(int i=0; i<prueba.getDados().size(); i++){
            aux=new ImageView(this);
            aux.setImageResource(prueba.getDados().get(i).getImg());
            skins.addView(aux);
    }
}
