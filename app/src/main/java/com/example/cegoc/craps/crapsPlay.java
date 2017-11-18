package com.example.cegoc.craps;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class crapsPlay extends AppCompatActivity {

    private final int MONEDAS_GANADAS=10;

    private final int MONEDAS_PERDIDAS=10;

    private AdView mAdView;
    private String arrDado[];
    private ImageView img1;
    private ImageView img2;
    private TextView tiradaText, monedasText, rondaText;
    private boolean control, hasJugado;
    private int dado1, dado2, valorTirada1, monedas, contadorRondas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.craps_play);

        // Inicializo anuncio
        MobileAds.initialize(this, getResources().getString(R.string.id_app_adTest));
        mAdView = (AdView) findViewById(R.id.adView);
        cargaAnuncio();

        // ToDo comprobar las monedas guardadas
        monedas=0;

        //ToDo Comprobar que color de dado guardado (el array cambia)

        arrDado=getResources().getStringArray(R.array.dadosRojo);

        img1=(ImageView) findViewById(R.id.dado1);
        int resID = getResources().getIdentifier(arrDado[5], "drawable", getPackageName());
        img1.setImageResource(resID);
        img2=(ImageView) findViewById(R.id.dado2);
        resID = getResources().getIdentifier(arrDado[5], "drawable", getPackageName());
        img2.setImageResource(resID);
        LinearLayout dadosLayout=(LinearLayout)findViewById(R.id.dados);
        tiradaText=(TextView)findViewById(R.id.tiradaRef);
        monedasText=(TextView) findViewById(R.id.monedas);
        rondaText=(TextView) findViewById(R.id.numRonda);

        estadoInicial();

        dadosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCraps();
            }
        });
    }

    private void cargaAnuncio(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {}

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(crapsPlay.this, "adFailedToLoad", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cargaAnuncio();
                    }
                }, 10000);
            }

            @Override
            public void onAdOpened() {}

            @Override
            public void onAdLeftApplication() {}

            @Override
            public void onAdClosed() {}
        });
    }

    /**
     * Metodo que gestiona el juego
     */
    private void playCraps(){
        control=!control;
        tirarDados();
        if(!hasJugado){
            valorTirada1=primeraRonda();
            if(valorTirada1!=0){
                hasJugado=true;
            }
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
        if(!control){
            contadorRondas++;
            rondaText.setText
                    (String.format(getResources().getString(R.string.rondas), contadorRondas));
            int total=dado1+dado2;

            switch (total) {
                case 7:
                case 11:
                    // Ganas
                    Toast.makeText(this, ("+"+MONEDAS_GANADAS+" "+
                                    getResources().getString(R.string.toastMoneda)),
                            Toast.LENGTH_SHORT).show();
                    monedas += MONEDAS_GANADAS;
                    estadoInicial();
                    break;
                case 2:
                case 3:
                case 12:
                    // Pierdes
                    if (monedas <= 8) {
                        monedas = 0;
                    } else {
                        monedas -= MONEDAS_PERDIDAS;
                    }
                    Toast.makeText(this, ("-"+MONEDAS_PERDIDAS+" "+
                                    getResources().getString(R.string.toastMoneda)),
                            Toast.LENGTH_SHORT).show();
                    estadoInicial();
                    break;
                default:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tiradaText.setTextColor(ContextCompat.getColor(crapsPlay.this,
                                    R.color.numeroTargetActivo));
                            tiradaText.setText(String.valueOf(dado1+dado2));
                        }
                    }, 150);
                    return total;
            }
        }
        return 0;
    }

    /**
     * Simula la segunda ronda y las consecutivas en caso de que se den.
     * Si se saca un 7 se pierde
     * Si se saca el mismo numero que en la ronda 1 se gana
     */
    private void rondas() {
        if(!control) {
            contadorRondas++;
            rondaText.setText
                    (String.format(getResources().getString(R.string.rondas), contadorRondas));
            if (valorTirada1 == (dado1 + dado2)) {
                // Ganas
                Toast.makeText(this, ("+" + MONEDAS_GANADAS + " " +
                                getResources().getString(R.string.toastMoneda)),
                        Toast.LENGTH_SHORT).show();
                monedas += MONEDAS_GANADAS;
                estadoInicial();
            } else if ((dado1 + dado2) == 7) {
                // Pierdes
                if (monedas <= 8) {
                    monedas = 0;
                } else {
                    monedas -= MONEDAS_PERDIDAS;
                }
                Toast.makeText(this, ("-" + MONEDAS_PERDIDAS + " " +
                                getResources().getString(R.string.toastMoneda)),
                        Toast.LENGTH_SHORT).show();
                estadoInicial();
            }
        }
    }

    /**
     * Resea al valor inicial
     */
    private void estadoInicial(){
        hasJugado=false;
        control=false;
        tiradaText.setTextColor(ContextCompat.getColor(this, R.color.numeroTargetDesactivado));
        valorTirada1=0;
        contadorRondas=0;
        rondaText.setText
                (String.format(getResources().getString(R.string.rondas), contadorRondas));
        monedasText.setText(String.valueOf(monedas));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tiradaText.setText(String.valueOf(dado1+dado2));
                tiradaText.setTextColor(ContextCompat.getColor(crapsPlay.this,
                        R.color.numeroTargetDesactivado));
            }
        }, 200);
    }
}