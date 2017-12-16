package com.example.cegoc.craps;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class crapsPlay extends AppCompatActivity {

    private final double MULTIPLICADOR=1.5;
    private final int APUESTA_INICIAL=10;

    private MediaPlayer dadosSound;
    private AdView mAdView;
    private String arrDado[];
    private LinearLayout dadosLayout;
    private ImageView img1, img2;
    private Button botonNada, botonDoble;
    private TextView tiradaText, monedasText, rondaText;
    private boolean control, hasJugado, all_in;
    private int dado1, dado2, valorTirada1, monedas, contadorRondas, apuestaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.craps_play);

        init();
        estadoInicial();
        cargaAnuncio();

        botonNada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        estadoInicial();
                        dadosLayout.setClickable(true);
                        muestraBotones(false);
                    }
                }, 200);
            }
        });
        //ToDo trabajar mas aqui
        botonDoble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((apuestaActual*MULTIPLICADOR)>=monedas){
                    // All-in
                    all_in=true;
                    apuestaActual+=monedas;
                    monedas=0;
                } else{
                    apuestaActual*=MULTIPLICADOR;
                    monedas-=apuestaActual;
                }
                Toast.makeText(crapsPlay.this, "Apuesta: "+apuestaActual,
                        Toast.LENGTH_SHORT).show();
                dadosLayout.setClickable(true);
                monedasText.setText(String.valueOf(monedas));
                muestraBotones(false);
            }
        });

        dadosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCraps();
            }
        });
    }

    /**
     * Metodo que carga un anuncio
     * Cuando el anuncio no carga, vuelve a intentar cargarlo a los 10 segundos
     */
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
            if (!hasJugado) {
                if(monedas-APUESTA_INICIAL>=0) {
                    tirarDados();
                    monedas -= APUESTA_INICIAL;
                    monedasText.setText(String.valueOf(monedas));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hasJugado = true;
                            valorTirada1 = primeraRonda();
                        }
                    }, 1521);
                } else {
                    Toast.makeText(crapsPlay.this, "No tienes suficientes monedas",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                tirarDados();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rondas();
                    }
                }, 1521);
            }
    }


    /**
     * Metodo auxiliar que hace que los dados cambien de valor cada 100 millsegundos
     * mientras control sea 'true'
     */
    private void tirarDadosAux(){
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
                    tirarDadosAux();
                }
            }, 100);
        }
    }

    /**
     * Metodo que gestiona una tirada de dados, con el sonido incluido
     */
    private void tirarDados(){
        muestraBotones(false);
        dadosLayout.setClickable(false);
        control=true;
        tirarDadosAux();
        dadosSound.start();
        new CountDownTimer(1520, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                control=false;
            }
        }.start();
    }

    /**
     * Simula la primera ronda del juego
     * Ganas con 7 o 11 (MONEDAS_GANADAS)
     * Pierdes con 2, 3 o 12 (MONEDAS_PERDIDAS)
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
                    finPartida(true);
                    break;
                case 2:
                case 3:
                case 12:
                    finPartida(false);
                    break;
                default:
                    tiradaText.setTextColor(ContextCompat.getColor(crapsPlay.this,
                            R.color.numeroTargetActivo));
                    tiradaText.setText(String.valueOf(dado1+dado2));
                    muestraBotones(true);
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
                finPartida(true);
            } else if ((dado1 + dado2) == 7) {
                finPartida(false);
            } else{
                if(all_in){
                    dadosLayout.setClickable(true);
                }else{
                    muestraBotones(true);
                }
            }
        }
    }

    /**
     * Resea al valor inicial
     */
    private void estadoInicial(){
        hasJugado=false;
        control=false;
        all_in=false;
        valorTirada1=0;
        contadorRondas=0;
        apuestaActual=APUESTA_INICIAL;
        muestraBotones(false);
        dadosLayout.setClickable(true);
        rondaText.setText
                (String.format(getResources().getString(R.string.rondas), contadorRondas));
        monedasText.setText(String.valueOf(monedas));
        tiradaText.setText(String.valueOf(dado1+dado2));
        tiradaText.setTextColor(ContextCompat.getColor(crapsPlay.this,
                R.color.numeroTargetDesactivado));
    }

    /**
     * Muestra los botones "Nada" y "Doble"
     * @param cond true para mostrar, false para ocultar
     */
    private void muestraBotones(boolean cond){
        if(cond){
            botonDoble.setVisibility(View.VISIBLE);
            botonNada.setVisibility(View.VISIBLE);
        } else{
            botonDoble.setVisibility(View.INVISIBLE);
            botonNada.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Metodo que hace todas las inicializaciones
     */
    private void init(){
        // Inicializo anuncio
        MobileAds.initialize(this, getResources().getString(R.string.id_app_adTest));
        mAdView = (AdView) findViewById(R.id.adView);

        //ToDo Coger monedas del usuario en el sharedpreferences
        monedas=500;

        arrDado=getResources().getStringArray(R.array.dadosNormal);
        dadosSound=MediaPlayer.create(crapsPlay.this,R.raw.dados2);
        img1=(ImageView) findViewById(R.id.dado1);
        int resID = getResources().getIdentifier(arrDado[5], "drawable", getPackageName());
        img1.setImageResource(resID);
        img2=(ImageView) findViewById(R.id.dado2);
        resID = getResources().getIdentifier(arrDado[5], "drawable", getPackageName());
        img2.setImageResource(resID);
        dadosLayout=(LinearLayout)findViewById(R.id.dados);
        tiradaText=(TextView)findViewById(R.id.tiradaRef);
        monedasText=(TextView) findViewById(R.id.monedas);
        rondaText=(TextView) findViewById(R.id.numRonda);
        botonDoble=(Button) findViewById(R.id.botonDoble);
        botonNada=(Button) findViewById(R.id.botonNada);
    }

    /**
     * Metodo para gestionar el ganar o perder de la partida
     * @param estado true para ganar, false para perder
     */
    private void finPartida(boolean estado){
        if(estado){
            // Ganar
            Toast.makeText(this, ("+"+(int)(apuestaActual*MULTIPLICADOR)+" "+
                            getResources().getString(R.string.toastMoneda)),
                    Toast.LENGTH_SHORT).show();
            monedas += apuestaActual*MULTIPLICADOR;

        } else{
            // Perder
            Toast.makeText(this, ("Perdiste"),
                    Toast.LENGTH_SHORT).show();
        }
        estadoInicial();
    }
}