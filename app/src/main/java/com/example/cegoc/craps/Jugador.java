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
        this.monedas = 10;

        // Todos los avatares de la app
        avatares.add(new Item(R.drawable.avatar1, true, 0));
        avatares.add(new Item(R.drawable.avatar2, false, 10));
        avatares.add(new Item(R.drawable.avatar3, false, 20));
        avatares.add(new Item(R.drawable.avatar4, false, 30));

        // Avatar predeterminado del usuario
        this.avatarActual=avatares.get(0).getImg();
    }

    public String getNombre() {
        return nombre;	}

    public String getClave() {
        return clave;	}

    public int getMonedas(){
        return monedas;
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
