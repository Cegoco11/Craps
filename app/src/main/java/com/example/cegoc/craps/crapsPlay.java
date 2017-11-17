package com.example.cegoc.craps;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class crapsPlay extends AppCompatActivity {

    private final int MONEDAS_GANADAS=10;
    private final int MONEDAS_PERDIDAS=8;
    private String arrDado[];
    private ImageView img1;
    private ImageView img2;
    private LinearLayout dadosLayout;
    private TextView tiradaText;
    private boolean control,hasJugado;
    private int dado1, dado2, valorTirada1, monedas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.craps_play);

        // ToDo comprobar en save las monedas
        monedas=0;

        estadoInicial();

        arrDado=getResources().getStringArray(R.array.dadosGris);
        img1=(ImageView) findViewById(R.id.dado1);
        img2=(ImageView) findViewById(R.id.dado2);
        dadosLayout=(LinearLayout)findViewById(R.id.dados);
        tiradaText=(TextView)findViewById(R.id.tiradaRef);

        dadosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCraps();
            }
        });
    }

    /**
     * Metodo que gestiona el juego
     */
    private void playCraps(){
        control=!control;
        if(!hasJugado){
            valorTirada1=primeraRonda();
            tiradaText.setText(valorTirada1);
            hasJugado=true;
        } else{
            rondas();
        }
    }

    /**
     * Mientras control sea 'true' los dados se mueven
     * Cuando control pasa a 'false' los dados se detienen
     */
    private void tirarDados(){
        if(control) {
            int resID;
            String aux;
            dado1 = (int) (Math.random()*6+1);
            dado2 = (int) (Math.random()*6+1);

            aux = arrDado[dado1-1];
            resID = getResources().getIdentifier(aux, "drawable", getPackageName());
            img1.setImageResource(resID);
            img1.setVisibility(View.VISIBLE);

            aux = arrDado[dado2-1];
            resID = getResources().getIdentifier(aux, "drawable", getPackageName());
            img2.setImageResource(resID);
            img2.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tirarDados();
                }
            }, 100);
        }
    }

    /**
     * Simula la primera ronda del juego
     * Ganas con 7 o 11 (10 monedas)
     * Pierdes con 2, 3 o 12 (8 monedas)
     * @return El numero que ha salido o 0 si ya ha finalizado
     */
    private int primeraRonda() {
        tirarDados();
        int total=dado1+dado2;
        switch (total) {
            case 7:
            case 11:
                // Ganas
                estadoInicial();
                Toast.makeText(this, "+"+MONEDAS_GANADAS+ " "+R.string.toastMoneda,
                        Toast.LENGTH_SHORT).show();
                monedas += MONEDAS_GANADAS;
                break;
            case 2:
            case 3:
            case 12:
                // Pierdes
                estadoInicial();
                if (monedas <= 8) {
                    monedas = 0;
                } else {
                    monedas -= MONEDAS_PERDIDAS;
                }
                Toast.makeText(this, "-"+MONEDAS_PERDIDAS+" "+R.string.toastMoneda,
                        Toast.LENGTH_SHORT).show();
                break;
        }
        return total;
    }

    /**
     * Simula la segunda ronda y las consecutivas en caso de que se den.
     * Si se saca un 7 se pierde
     * Si se saca el mismo numero que en la ronda 1 se gana
     */
    private void rondas() {
        tirarDados();
        if (valorTirada1 == (dado1+dado2)) {
            // Ganas
            estadoInicial();
            Toast.makeText(this, "+"+MONEDAS_GANADAS+ " "+R.string.toastMoneda,
                    Toast.LENGTH_SHORT).show();
            monedas += MONEDAS_GANADAS;
        }
        if((dado1+dado2)==7){
            // Pierdes
            estadoInicial();
            if (monedas <= 8) {
                monedas = 0;
            } else {
                monedas -= MONEDAS_PERDIDAS;
            }
            Toast.makeText(this, "-"+MONEDAS_PERDIDAS+" "+R.string.toastMoneda,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Resea al valor inicial
     */
    private void estadoInicial(){
        control=false;
        hasJugado=false;
        tiradaText.setText("");
    }
}
