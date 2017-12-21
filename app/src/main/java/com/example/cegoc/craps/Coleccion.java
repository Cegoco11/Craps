package com.example.cegoc.craps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Clase que muestra en el layout coleccion el inventario desbloqueado del jugador
 *
 * @author Caesar
 */
public class Coleccion extends AppCompatActivity {

    private GridLayout avatares, skins;
    private SharedPreferences prefe;
    private Jugador prueba;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coleccion);

        prefe=getSharedPreferences("Active_User", Context.MODE_PRIVATE);

        // Usuario de prueba para comprobar que funciona correctamente
        // Aqui habria que tener en cuenta quien es el usuario actual
        prueba=new Jugador("Cesar","asd", "asadsasad");

        avatares=(GridLayout) findViewById(R.id.AvataresGrid);
        ImageView aux;
        // Muestro todos los avatares, hay que filtrar por los que estan desbloqueados
        for(i=0; i<prueba.getAvatares().size(); i++){
            if(prueba.getAvatares().get(i).getEstado()){
                aux=new ImageView(this);
                aux.setId(i);
                aux.setImageResource(prueba.getAvatares().get(i).getImg());
                aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = prefe.edit();
                        if(prefe.getInt("avatar", -1)==prueba.getAvatares().get(v.getId()).getImg()){
                            Toast.makeText(Coleccion.this, "Already equipped", Toast.LENGTH_SHORT).show();
                        } else{
                            editor.putInt("avatar", prueba.getAvatares().get(v.getId()).getImg());
                            editor.commit();
                            Toast.makeText(Coleccion.this, "Equipped", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                avatares.addView(aux);
            }
        }
        skins=(GridLayout) findViewById(R.id.SkinsGrid);
        for(i=0; i<prueba.getDados().size(); i++){
            if(prueba.getDados().get(i).getEstado()){
                aux=new ImageView(this);
                aux.setImageResource(prueba.getDados().get(i).getImg());
                aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = prefe.edit();
                        editor.putInt("skin", prueba.getDados().get(i).getDados());
                        editor.commit();
                        Toast.makeText(Coleccion.this, "Equipped2", Toast.LENGTH_SHORT).show();
                    }
                });
                skins.addView(aux);
            }
        }
    }
}
