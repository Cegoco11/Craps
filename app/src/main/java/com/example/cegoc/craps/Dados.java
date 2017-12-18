package com.example.cegoc.craps;

import java.lang.reflect.Array;

/**
 * Clase hija que contiene una referencia al array con las imagenes de los dados
 *
 * @author Caesar
 */

public class Dados extends Item {

    private int dados; // Referencia al array guardado en strings.xml

    public Dados(int img, boolean estado, int precio){
        super(img, estado, precio);
        this.dados=R.array.dadosNormal;
    }

    public int getDados(){
        return dados;
    }
}
