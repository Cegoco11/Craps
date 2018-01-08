package com.example.cegoc.craps;

import java.io.Serializable;

/**
 * Contiene la informacion de un objeto (avatar) y es la clase padre
 *
 * @author Caesar
 */

public class Item implements Serializable {
    protected int img; // Referencia a la imagen guardada en drawables
    protected int img_bloq;
    protected boolean estado;
    protected int precio;

    public Item(int i, boolean e, int p, int b){
        this.img=i;
        this.estado=e;
        this.precio=p;
        this.img_bloq=b;
    }

    public void compra(){
        estado=true;
    }

    public int getImg(){
        return img;
    }

    public int getImg_bloq(){
        return img_bloq;
    }

    public int getPrecio(){
        return precio;
    }

    public boolean getEstado(){
        return estado;
    }
}
