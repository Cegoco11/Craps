package com.example.cegoc.craps;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class crapsPlay extends AppCompatActivity {

    private String arrDado[];
    private ImageView img1;
    private ImageView img2;
    private LinearLayout dadosLayout;
    private boolean control;
    private int dado1, dado2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.craps_play);

        control=false;
        arrDado=getResources().getStringArray(R.array.dadosGris);
        img1=(ImageView) findViewById(R.id.dado1);
        img2=(ImageView) findViewById(R.id.dado2);
        dadosLayout=(LinearLayout)findViewById(R.id.dados);

        dadosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control=!control;
                tirarDados();
            }
        });
    }

    /**
     * Se simula que se tiran 2 dados
     * @return
     */
    private void tirarDados(){
        if(control) {
            int resID;
            String aux;
            dado1 = (int) (Math.random() * 6);
            dado2 = (int) (Math.random() * 6);

            aux = arrDado[dado1];
            resID = getResources().getIdentifier(aux, "drawable", getPackageName());
            img1.setImageResource(resID);
            img1.setVisibility(View.VISIBLE);

            aux = arrDado[dado2];
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
}
