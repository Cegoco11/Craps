package com.example.cegoc.craps;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Solo se muestran los items y dependiendo de si pulsas en uno bloqueado o no, te  dice si ya lo
 * tienes o que estamos en WIP
 */
public class tienda extends AppCompatActivity {

    private GridLayout avatares, skins;
    private int i;
    private Jugador prueba;
    private SharedPreferences prefe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Oculta Titulo de la ventanagetSupportActionBar().hide(); // Oculta Titulo de la ventana
        setContentView(R.layout.coleccion);

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
                tv_aux.setTypeface(Pixel1);
                ll_aux.setId(i); // Pongo de id la interaccion
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
                tv_aux.setTypeface(Pixel1);
                ll_aux.setId(i); // Pongo de id la interaccion
                ll_aux.addView(tv_aux);
                ll_aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Comprar
                        if(!prueba.getNombre().equals("Guest")){
                            if(prueba.getMonedas()>=prueba.getAvatares().get(v.getId()).getPrecio()){
                                prueba.setMonedas(prueba.getMonedas()-prueba.getAvatares().get(v.getId()).getPrecio());
                                prueba.getAvatares().get(v.getId()).compra();
                                guardaJugador(prueba);
                                Toast.makeText(tienda.this,"Comprado con exito", Toast.LENGTH_SHORT).show();
                                recreate();
                            } else{
                                Toast.makeText(tienda.this,"No tienes suficientes monedas", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(tienda.this,"Invitado detected", Toast.LENGTH_SHORT).show();
                        }

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
            if (prueba.getDados().get(i).getEstado()) {
                aux = new ImageView(this);
                aux.setImageResource(prueba.getDados().get(i).getImg());
                ll_aux.addView(aux);
                tv_aux=new TextView(this);
                tv_aux.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv_aux.setText(String.valueOf(prueba.getDados().get(i).getPrecio()));
                tv_aux.setTypeface(Pixel1);
                ll_aux.setId(i); // Pongo de id la interaccion
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
                tv_aux.setTypeface(Pixel1);
                ll_aux.setId(i); // Pongo de id la interaccion
                ll_aux.addView(tv_aux);
                ll_aux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Comprar
                        if(!prueba.getNombre().equals("Guest")){
                            if(prueba.getMonedas()>=prueba.getDados().get(v.getId()).getPrecio()){
                                prueba.setMonedas(prueba.getMonedas()-prueba.getDados().get(v.getId()).getPrecio());
                                prueba.getDados().get(v.getId()).compra();
                                guardaJugador(prueba);
                                Toast.makeText(tienda.this,"Comprado con exito", Toast.LENGTH_SHORT).show();
                                recreate();
                            } else{
                                Toast.makeText(tienda.this,"No tienes suficientes monedas", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(tienda.this,"Invitado detected", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
            skins.addView(ll_aux);
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
     * @param player Jugador en el que se va a escribir
     */
    public void guardaJugador(Jugador player) {
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
