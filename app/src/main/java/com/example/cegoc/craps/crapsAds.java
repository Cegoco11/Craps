package com.example.cegoc.craps;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Caesar
 */
public class CrapsAds extends AppCompatActivity implements RewardedVideoAdListener {

    private RewardedVideoAd mRewardedVideoAd;
    private ProgressBar progress;
    private TextView monedasText;
    private Button cargaAnuncio;
    private int monedas=0;
    private CountDownTimer countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.craps_ads);

        MobileAds.initialize(this, getResources().getString(R.string.id_app_adTest));

        cargaAnuncio=(Button)findViewById(R.id.carga);
        Button muestraAnuncio=(Button)findViewById(R.id.muestra);

        progress=(ProgressBar)findViewById(R.id.progressBar2);
        monedasText=(TextView) findViewById(R.id.monedas);
        monedasText.setText(String.valueOf(monedas));

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        tiempoEspera(1); // 1 min de espera hasta poder conseguir monedas

        cargaAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRewardedVideoAd();
                progress.setVisibility(View.VISIBLE);
                cargaAnuncio.setEnabled(false);
            }
        });
        muestraAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else{
                    creaAlerta(getResources().getString(R.string.tituloDialog),
                            getResources().getString(R.string.anuncioNoCargo)).show();
                }
            }
        });
    }

    /**
     * Metodo auxiliar para crear DialogAlert en pantalla
     *
     * @param titulo  titulo de la ventana
     * @param mensaje mensaje que queremos mostrar
     * @return un dialogo, hay que usar .show() para que se muestre en pantalla
     */
    private Dialog creaAlerta(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje).setTitle(titulo);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No se
            }
        });
        return builder.create();
    }

    /**
     * Metodo auxiliar que desactiva el boton, crea un contador, que muestra el tiempo que falta
     * en el text del boton, y cuando llega a 0 activa el boton de nuevo con su texto
     * original
     * @param t tiempo en minutos
     */
    private void tiempoEspera(long t){
        cargaAnuncio.setEnabled(false);
        countDown=new CountDownTimer(t*60*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long aux=millisUntilFinished;
                int minutes=(int)TimeUnit.MILLISECONDS.toMinutes(aux);
                aux=aux-TimeUnit.MINUTES.toMillis(minutes);
                int seconds=(int)TimeUnit.MILLISECONDS.toSeconds(aux);

                cargaAnuncio.setText(getResources().getString(R.string.cuentaAtrasBoton) +" "+
                minutes+":"+seconds);
            }

            public void onFinish() {
                cargaAnuncio.setText(getResources().getString(R.string.cargaAnuncio));
                cargaAnuncio.setEnabled(true);
            }
        }.start();
    }

    /**

     * Metodo que carga un anuncio
     */
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getResources().getString(R.string.id_adTestVideo),
                new AdRequest.Builder().build());

    }

    @Override
    public void onRewarded(RewardItem reward) {
        countDown.cancel();
        tiempoEspera(1);
        creaAlerta(getResources().getString(R.string.tituloDialog),
                getResources().getString(R.string.premio)).show();
        monedas=monedas+30;
        monedasText.setText(String.valueOf(monedas));
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        countDown.cancel();
        tiempoEspera(1);
    }

    @Override
    public void onRewardedVideoAdClosed() {
        countDown.cancel();
        tiempoEspera(1);
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        progress.setVisibility(View.GONE);
        cargaAnuncio.setEnabled(true);
        creaAlerta(getResources().getString(R.string.tituloDialog),
                getResources().getString(R.string.anuncioNoCargo)).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        progress.setVisibility(View.GONE);
        creaAlerta(getResources().getString(R.string.tituloDialog),
                getResources().getString(R.string.anuncioCargado)).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {}

    @Override
    public void onRewardedVideoStarted() {tiempoEspera(1);}

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}