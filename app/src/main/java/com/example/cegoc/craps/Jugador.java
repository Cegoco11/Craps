package com.example.cegoc.craps;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Marina on 25-Nov-17.
 */

public class Jugador implements Serializable {

    private String nombre; //Nombre del usuario
    private String clave;
    private int monedas;

    private ArrayList<Boolean> baraja1; // Array que guarda las cartas disponibles de la baraja del tipo 1
    private ArrayList<Boolean> baraja2; // Array que guarda las cartas disponibles de la baraja del tipo 2
    private ArrayList<Boolean> baraja3; // Array que guarda las cartas disponibles de la baraja del tipo 3
    private ArrayList<Boolean> baraja4; // Array que guarda las cartas disponibles de la baraja del tipo 4
    private ArrayList<Boolean> baraja5; // Array que guarda las cartas disponibles de la baraja del tipo 5


    public Jugador(String nombre, String clave){
        this.nombre = nombre;
        this.clave = getMD5(clave);
        this.monedas = 10;

        this.baraja1 = new ArrayList<>(50);
        this.baraja2 = new ArrayList<>(50);
        this.baraja3 = new ArrayList<>(50);
        this.baraja4 = new ArrayList<>(50);
        this.baraja5 = new ArrayList<>(50);
    }


    public String mostrarnombre() {
        return nombre;	}

    public int mostrarmonedas(){
        return monedas;

    }

    public void add_coin(int cantidad){
        monedas = monedas + cantidad;
    }

    public void unblock_card(int baraja, int posicion){

        switch(baraja){
            case 1:
                baraja1.add(posicion,true);
                break;
            case 2:
                baraja1.add(posicion,true);
                break;
            case 3:
                baraja1.add(posicion,true);
                break;
            case 4:
                baraja1.add(posicion,true);
                break;
            case 5:
                baraja1.add(posicion,true);
                break;
            default:
                break;
        }
    }
    private static String getMD5(String input) {
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
