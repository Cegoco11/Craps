package com.example.cegoc.craps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Clase que muestra en el layout coleccion el inventario desbloqueado del jugador
 *
 * @author Caesar
 */
public class Coleccion extends AppCompatActivity {

    private GridLayout avatares, skins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coleccion);
        getSupportActionBar().hide(); // Oculta Titulo de la ventana

        // Usuario de prueba para comprobar que funciona correctamente
        // Aqui habria que tener en cuenta quien es el usuario actual
        Jugador prueba=new Jugador("Cesar","asd", "asadsasad");

        avatares=(GridLayout) findViewById(R.id.AvataresGrid);
        ImageView aux;
        // Muestro todos los avatares, hay que filtrar por los que estan desbloqueados
        for(int i=0; i<prueba.getAvatares().size(); i++){
            if(prueba.getAvatares().get(i).getEstado()){
                aux=new ImageView(this);
                aux.setImageResource(prueba.getAvatares().get(i).getImg());
                avatares.addView(aux);
            }
        }
        skins=(GridLayout) findViewById(R.id.SkinsGrid);
        // ToDo Mostrar skins
        for(int i=0; i<prueba.getAvatares().size(); i++){
            if(prueba.getDados().get(i).getEstado()){
                aux=new ImageView(this);
                aux.setImageResource(prueba.getDados().get(i).getImg());
                skins.addView(aux);
            }
        }
    }
}
