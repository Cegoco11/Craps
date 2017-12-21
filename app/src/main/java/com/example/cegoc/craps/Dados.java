package com.example.cegoc.craps;

import java.io.Serializable;

/**
 * Clase hija que contiene una referencia al array con las imagenes de los dados
 *
 * @author Caesar
 */

public class Dados extends Item implements Serializable {

    private int dados; // Referencia al array guardado en strings.xml

    public Dados(int img, boolean estado, int precio, int bloq){
        super(img, estado, precio, bloq);
        this.dados=R.array.dadosNormal;
    }

    public void setDados(int dados){
        this.dados=dados;
    }

    public int getDados(){
        return dados;
    }
}
