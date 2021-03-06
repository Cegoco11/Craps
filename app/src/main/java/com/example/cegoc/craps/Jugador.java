package com.example.cegoc.craps;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Marina on 25-Nov-17.
 * Edited by Caesar
 */

public class Jugador implements Serializable {

    private String nombre; //Nombre del usuario
    private String clave;
    private String correo;
    private int monedas;
    private int avatarActual;
    private int dadosActual;
    private ArrayList<Item> avatares=new ArrayList<Item>();
    private ArrayList<Dados> dados=new ArrayList<Dados>();

    public Jugador(String nombre, String clave, String correo){
        this.nombre = nombre;
        this.correo = correo;
        this.clave = getMD5(clave);
        this.monedas = 100;

        // Todos los avatares de la app
        avatares.add(new Item(R.drawable.invitado, true, 0, R.drawable.invitado));
        avatares.add(new Item(R.drawable.avatar3, true, 150, R.drawable.avatar3off));
        avatares.add(new Item(R.drawable.avatar4, false, 500, R.drawable.avatar4off));
        avatares.add(new Item(R.drawable.avatar1, false, 500, R.drawable.avatar1off));
        avatares.add(new Item(R.drawable.avatar2, true, 550, R.drawable.avatar2off));
        // Mas avatares
        avatares.add(new Item(R.drawable.avatar5, false, 550, R.drawable.avatar5off));
        avatares.add(new Item(R.drawable.avatar6, true, 550, R.drawable.avatar6off));
        avatares.add(new Item(R.drawable.avatar7, false, 550, R.drawable.avatar7off));
        avatares.add(new Item(R.drawable.avatar8, false, 550, R.drawable.avatar8off));
        avatares.add(new Item(R.drawable.avatar9, false, 550, R.drawable.avatar9off));
        avatares.add(new Item(R.drawable.avatar10, false, 550, R.drawable.avatar10off));
        avatares.add(new Item(R.drawable.avatar11, false, 550, R.drawable.avatar11off));
        avatares.add(new Item(R.drawable.avatar12, true, 550, R.drawable.avatar12off));
        avatares.add(new Item(R.drawable.avatar13, true, 550, R.drawable.avatar13off));
        avatares.add(new Item(R.drawable.avatar14, false, 550, R.drawable.avatar14off));
        avatares.add(new Item(R.drawable.avatar15, false, 550, R.drawable.avatar15off));
        avatares.add(new Item(R.drawable.avatar16, true, 550, R.drawable.avatar16off));

        dados.add(new Dados(R.drawable.icono_dado_normal, true, 0, R.drawable.icono_dado_normal,
                R.array.dadosNormal));
        dados.add(new Dados(R.drawable.icono_dado_rojo, true, 200, R.drawable.icono_bloq_dado_rojo,
                R.array.dadosRojo));
        dados.add(new Dados(R.drawable.icono_huevo, false, 500, R.drawable.icono_bloq_huevo,
                R.array.dadosHuevos));
        dados.add(new Dados(R.drawable.icono_dado_legendario, false, 1000,
                R.drawable.icono_bloq_dado_legendario, R.array.dadosLegendarios));

        // Avatar predeterminado
        this.avatarActual=avatares.get(0).getImg();
        // Skin predeterminada
        this.dadosActual=dados.get(0).getDados();
    }

    public String getNombre() {
        return nombre;	}

    public String getClave() {
        return clave;	}

    public int getMonedas(){
        return monedas;
    }

    public void setMonedas(int num){
        this.monedas=num;
    }

    public int getAvatarActual(){
        return avatarActual;
    }

    public void setAvatarActual(int a){
        this.avatarActual=a;
    }

    public int getDadosActual(){
        return dadosActual;
    }

    public void setDadosActual(int a){
        this.dadosActual=a;
    }

    public ArrayList<Item> getAvatares(){
        return avatares;
    }

    public ArrayList<Dados> getDados(){
        return dados;
    }

    public void add_coin(int cantidad){
        monedas = monedas + cantidad;
    }

    public void desbloquear(int baraja, int posicion){

    }

    /**
     * Metodo para encriptar un string en md5
     *
     * @param input String a encriptar
     * @return String encriptado
     */
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
