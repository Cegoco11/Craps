package com.example.cegoc.craps;

import java.io.Serializable;

/**
 * Contiene la informacion de un objeto (avatar) y es la clase padre
 *
 * @author Caesar
 */

public class Item implements Serializable {
    protected int img; // Referencia a la imagen guardada en drawables
    protected boolean estado;
    protected int precio;

    public Item(int i, boolean e, int p){
        this.img=i;
        this.estado=e;
        this.precio=p;
    }

    public void setDisponible(){
        estado=true;
    }

    public int getImg(){
        return img;
    }

    public int getPrecio(){
        return precio;
    }
}
