package com.example.cegoc.craps;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class CrapsSplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // Oculta barra de notificaciones
        setContentView(R.layout.craps_splashscreen);
        getSupportActionBar().hide(); // Oculta Titulo de la ventana
        progressBar=(ProgressBar) findViewById(R.id.determinateBar);
        progressBar.setMax(11);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < progressBar.getMax()+1) {
                    progressStatus=progressStatus+1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    if(progressStatus==progressBar.getMax()+1){
                        Intent intent = new Intent(CrapsSplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}