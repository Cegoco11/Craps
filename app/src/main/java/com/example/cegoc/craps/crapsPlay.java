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

    private final int APUESTA_INICIAL=10;
    private final int MONEDAS_GANADAS=10;
    private final int MONEDAS_PERDIDAS=10;

    private MediaPlayer dadosSound;
    private AdView mAdView;
    private String arrDado[];
    private LinearLayout dadosLayout;
    private ImageView img1, img2;
    private Button botonNada;
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

        botonNada=(Button) findViewById(R.id.botonNada);
        botonNada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonNada.setText(String.valueOf(contadorRondas));
            }
        });

        estadoInicial();

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
        if(!hasJugado){
            tirarDados();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    valorTirada1=primeraRonda();
                }
            }, 1521);
            hasJugado=true;
        } else{
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
        control=true;
        dadosLayout.setClickable(false);
        tirarDadosAux();
        dadosSound.start();
        new CountDownTimer(1520, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                control=false;
                dadosLayout.setClickable(true);
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

    /**
     * ToDo asdaasdasdaasd
     */
    private void sePuedeApostar(){
        int aux;
        if (contadorRondas == 0) {
            aux=1;
        } else{
            aux=contadorRondas;
        }

        if(monedas<(APUESTA_INICIAL*aux)){
            // No se puede apostar
        } else{
            monedas-=(APUESTA_INICIAL*aux);
        }
    }
}