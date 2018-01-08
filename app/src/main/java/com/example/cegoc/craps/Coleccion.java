package com.example.cegoc.craps;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
        getSupportActionBar().hide();

        final Typeface Pixel1=Typeface.createFromAsset(getAssets(), "Pixel1.ttf");

        prefe = getSharedPreferences("Active_User", Context.MODE_PRIVATE);

        TextView tv1=(TextView)findViewById(R.id.tv1);
        tv1.setTypeface(Pixel1);
        TextView tv2=(TextView)findViewById(R.id.tv2);
        tv2.setTypeface(Pixel1);

        prueba=cargarJugador();
        if(prueba==null){
            prueba = new Jugador("Guest", "123456A", "correo@correo.com"); // Invitado
        }


        avatares = (GridLayout) findViewById(R.id.AvataresGrid);
        ImageView aux;
        // Muestro todos los avatares, hay que filtrar por los que estan desbloqueados
        for (i = 0; i < prueba.getAvatares().size(); i++) {
            if (prueba.getAvatares().get(i).getEstado()) {
                aux = new ImageView(this);
                aux.setId(i); // Pongo de id la interaccion
                aux.setImageResource(prueba.getAvatares().get(i).getImg());
                aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Compruebo si lo que esta guardado en SharedPreferences es igual que el
                        // que estoy pulsando, si es asi sale "Already..." si no, lo mete al shared y muestra
                        // otro "Equipped"
                        if (prefe.getInt("avatar", -1) == prueba.getAvatares().get(v.getId()).getImg()) {
                            Toast.makeText(Coleccion.this, R.string.Yaequipado, Toast.LENGTH_SHORT).show();
                        } else {
                            if(!prueba.getNombre().equals("Guest")){
                                SharedPreferences.Editor editor = prefe.edit();
                                editor.putInt("avatar", prueba.getAvatares().get(v.getId()).getImg());
                                editor.commit();
                                guardaJugador(prueba, v.getId(), true);
                                Toast.makeText(Coleccion.this, R.string.equipado, Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(Coleccion.this, R.string.invitDetect, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                avatares.addView(aux);
            }
        }

        skins = (GridLayout) findViewById(R.id.SkinsGrid);
        for (i = 0; i < prueba.getDados().size(); i++) {
            if (prueba.getDados().get(i).getEstado()) {
                aux = new ImageView(this);
                aux.setId(i); // Pongo de id la interaccion
                aux.setImageResource(prueba.getDados().get(i).getImg());
                aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (prefe.getInt("skin", -1) == prueba.getDados().get(v.getId()).getDados()) {
                            Toast.makeText(Coleccion.this, R.string.Yaequipado, Toast.LENGTH_SHORT).show();
                        } else {
                            if (!prueba.getNombre().equals("Guest")) {
                                SharedPreferences.Editor editor = prefe.edit();
                                editor.putInt("skin", prueba.getDados().get(v.getId()).getDados());
                                editor.commit();
                                guardaJugador(prueba, v.getId(), false);
                                Toast.makeText(Coleccion.this, R.string.equipado, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Coleccion.this, R.string.invitDetect, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                skins.addView(aux);
            }
        }
    }

    /**
     * Metodo que carga el archivo del jugador activo (Beta)
     *
     * @return jugador si existe el fichero, si no retorna null
     */
    public Jugador cargarJugador() {
        Jugador aux = null;
        String nombre = prefe.getString("name", "Invitado");
        File file = getFileStreamPath(nombre);
        if (file.exists()) {
            FileInputStream fis;
            ObjectInputStream in = null;
            try {
                fis = openFileInput(nombre);
                in = new ObjectInputStream(fis);
                aux = (Jugador) in.readObject();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    /**
     *
     *
     * @param player Jugador en el que se va a escribir
     * @param idBoton id del boton que pulsamos
     * @param tipo true para avatar, false para dado
     */
    public void guardaJugador(Jugador player, int idBoton, boolean tipo) {
        if(tipo){
            player.setAvatarActual(player.getAvatares().get(idBoton).getImg());
        } else{
            player.setDadosActual(player.getDados().get(idBoton).getDados());
        }
        File file=getFileStreamPath(player.getNombre());
        if (file.exists()) {
            FileOutputStream fos;
            ObjectOutputStream out = null;
            try {
                fos = openFileOutput(player.getNombre(), Context.MODE_PRIVATE); //Guardamos cada objeto de la clase Jugador en un archivo en memoria que lleve por nombre el nombre del jugador
                out = new ObjectOutputStream(fos);
                out.writeObject(player);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            // No existe el archivo
        }
    }
}
