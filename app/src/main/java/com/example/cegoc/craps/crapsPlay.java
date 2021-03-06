package com.example.cegoc.craps;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Caesar
 */
public class crapsPlay extends AppCompatActivity {

    private final double MULTIPLICADOR = 1.2;
    private final int APUESTA_INICIAL = 10;

    private SharedPreferences prefe;
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
        getSupportActionBar().hide();

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
                if ((apuestaActual * MULTIPLICADOR) >= monedas) {
                    // All-in
                    all_in = true;
                    animacionContador(monedas, 0, monedasText);
                    apuestaActual += monedas;
                    monedas = 0;
                } else {
                    animacionContador(monedas, (int)Math.floor(monedas-apuestaActual*MULTIPLICADOR), monedasText);
                    apuestaActual = (int)Math.floor(apuestaActual*MULTIPLICADOR);
                    monedas -= apuestaActual;
                }
                Toast.makeText(crapsPlay.this, (String.format(getResources().getString(R.string.Apuesta), apuestaActual)),
                        Toast.LENGTH_SHORT).show();
                dadosLayout.setClickable(true);
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
    private void cargaAnuncio() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

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
            public void onAdOpened() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
            }
        });
    }

    /**
     * Metodo que gestiona el juego
     */
    private void playCraps() {
        if (!hasJugado) {
            if (monedas - APUESTA_INICIAL >= 0) {
                tirarDados();
                Toast.makeText(this, ( R.string.Apuesta_Inicial),
                        Toast.LENGTH_SHORT).show();
                animacionContador(monedas, monedas-APUESTA_INICIAL, monedasText);
                monedas -= APUESTA_INICIAL;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hasJugado = true;
                        valorTirada1 = primeraRonda();
                    }
                }, 1521);
            } else {
                Toast.makeText(crapsPlay.this, R.string.MonedasInsu,
                        Toast.LENGTH_SHORT).show();
                conseguirMonedas(dadosLayout);
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
    private void tirarDadosAux() {
        if (control) {
            int resID;
            String aux;

            dado1 = (int) (Math.random() * 6 + 1);
            dado2 = (int) (Math.random() * 6 + 1);

            aux = arrDado[dado1 - 1];
            resID = getResources().getIdentifier(aux, "drawable", getPackageName());
            img1.setImageResource(resID);
            img1.setVisibility(View.VISIBLE);

            aux = arrDado[dado2 - 1];
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
    private void tirarDados() {
        muestraBotones(false);
        dadosLayout.setClickable(false);
        control = true;
        tirarDadosAux();
        dadosSound.start();
        new CountDownTimer(1520, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                control = false;
            }
        }.start();
    }

    /**
     * Simula la primera ronda del juego
     * Ganas con 7 o 11 (MONEDAS_GANADAS)
     * Pierdes con 2, 3 o 12 (MONEDAS_PERDIDAS)
     *
     * @return El numero que ha salido o 0 si ya ha finalizado
     */
    private int primeraRonda() {
        if (!control) {
            contadorRondas++;
            rondaText.setText
                    (String.format(getResources().getString(R.string.rondas), contadorRondas));
            int total = dado1 + dado2;

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
                    tiradaText.setText(String.valueOf(dado1 + dado2));
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
        if (!control) {
            contadorRondas++;
            rondaText.setText
                    (String.format(getResources().getString(R.string.rondas), contadorRondas));
            if (valorTirada1 == (dado1 + dado2)) {
                finPartida(true);
            } else if ((dado1 + dado2) == 7) {
                finPartida(false);
            } else {
                if (all_in) {
                    dadosLayout.setClickable(true);
                } else {
                    muestraBotones(true);
                }
            }
        }
    }

    /**
     * Resea al valor inicial
     */
    private void estadoInicial() {
        hasJugado = false;
        control = false;
        all_in = false;
        valorTirada1 = 0;
        contadorRondas = 0;
        apuestaActual = APUESTA_INICIAL;
        muestraBotones(false);
        dadosLayout.setClickable(true);
        rondaText.setText
                (String.format(getResources().getString(R.string.rondas), contadorRondas));
        tiradaText.setText(String.valueOf(dado1 + dado2));
        tiradaText.setTextColor(ContextCompat.getColor(crapsPlay.this,
                R.color.numeroTargetDesactivado));
    }

    /**
     * Muestra los botones "Nada" y "Doble"
     *
     * @param cond true para mostrar, false para ocultar
     */
    private void muestraBotones(boolean cond) {
        if (cond) {
            botonDoble.setVisibility(View.VISIBLE);
            botonNada.setVisibility(View.VISIBLE);
        } else {
            botonDoble.setVisibility(View.INVISIBLE);
            botonNada.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Metodo que hace todas las inicializaciones
     */
    private void init() {
        final Typeface Pixel1=Typeface.createFromAsset(getAssets(), "Pixel1.ttf");
        // Inicializo anuncio
        MobileAds.initialize(this, getResources().getString(R.string.id_app_adTest));
        mAdView = (AdView) findViewById(R.id.adView);

        // Se cogen las monedas y skin del usuario actual que esta en el SharedPreferences
        prefe=getSharedPreferences("Active_User", Context.MODE_PRIVATE);
        prefe.getInt("skin", 0);
        monedas = prefe.getInt("coins", 0);
        int skinDadoJugador= prefe.getInt("skin", R.array.dadosNormal);
        arrDado = getResources().getStringArray(skinDadoJugador);

        dadosSound = MediaPlayer.create(crapsPlay.this, R.raw.dados2);
        img1 = (ImageView) findViewById(R.id.dado1);
        int resID = getResources().getIdentifier(arrDado[5], "drawable", getPackageName());
        img1.setImageResource(resID);
        img2 = (ImageView) findViewById(R.id.dado2);
        resID = getResources().getIdentifier(arrDado[5], "drawable", getPackageName());
        img2.setImageResource(resID);
        dadosLayout = (LinearLayout) findViewById(R.id.dados);
        tiradaText = (TextView) findViewById(R.id.tiradaRef);
        tiradaText.setTypeface(Pixel1);
        monedasText = (TextView) findViewById(R.id.monedas);
        // monedasText.setTypeface(Pixel1); // No queda bien al hacer la animacion
        rondaText = (TextView) findViewById(R.id.numRonda);
        rondaText.setTypeface(Pixel1);
        botonDoble = (Button) findViewById(R.id.botonDoble);
        botonNada = (Button) findViewById(R.id.botonNada);
        monedasText.setText(String.valueOf(monedas));
    }

    /**
     * Metodo para gestionar el ganar o perder de la partida
     *
     * @param estado true para ganar, false para perder
     */
    private void finPartida(boolean estado) {
        if (estado) {
            // Ganar
            Toast.makeText(this, ("+" + (int) (apuestaActual * MULTIPLICADOR) + " " +
                            getResources().getString(R.string.toastMoneda)),
                    Toast.LENGTH_SHORT).show();
            animacionContador(monedas, (int)Math.floor(monedas+apuestaActual*MULTIPLICADOR), monedasText);
            monedas = (int)Math.floor(apuestaActual + apuestaActual * MULTIPLICADOR);
            Jugador aux=cargarJugador();
            if(aux==null){
                aux = new Jugador("Guest", "123456A", "correo@correo.com"); // Invitado
            }
            aux.setMonedas(monedas);
            guardaJugador(aux);
        } else {
            // Perder
            Toast.makeText(this, ( R.string.Perdiste),
                    Toast.LENGTH_SHORT).show();
        }
        estadoInicial();
    }

    /**
     * Metodo que hace la animacion del contador de monedas
     *
     * @param start valor inicial
     * @param end valor final
     * @param vista TextView al que se lo aplicamos
     */
    public static void animacionContador(int start, int end, final TextView vista) {
        if(end<0){
            end=0;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(350);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                vista.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();
    }

    /**
     *
     */
    public void muestraAyuda(View v){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(R.string.tuto).setTitle("Ayuda");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();
    }

    public void conseguirMonedas(View v){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Quieres conseguir un bonus de monedas?").setTitle("Conseguir monedas (WIP)");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(crapsPlay.this, crapsAds.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public Jugador cargarJugador() {
        Jugador aux = null;
        String nombre = prefe.getString("name", "Invitado");
        File file = getFileStreamPath(nombre);
        if (file.exists()) {
            FileInputStream fis;
            ObjectInputStream in = null;
            try {
                fis = openFileInput(nombre);
                in = new ObjectInputStream(fis);
                aux = (Jugador) in.readObject();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    public void guardaJugador(Jugador player) {
        File file=getFileStreamPath(player.getNombre());
        if (file.exists()) {
            FileOutputStream fos;
            ObjectOutputStream out = null;
            try {
                fos = openFileOutput(player.getNombre(), Context.MODE_PRIVATE); //Guardamos cada objeto de la clase Jugador en un archivo en memoria que lleve por nombre el nombre del jugador
                out = new ObjectOutputStream(fos);
                out.writeObject(player);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            // No existe el archivo
        }
    }
}