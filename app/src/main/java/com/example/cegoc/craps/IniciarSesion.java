package com.example.cegoc.craps;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *
 * @author Pablo
 */

public class IniciarSesion extends AppCompatActivity{


        private EditText txtusuario;
        private EditText txtclave;
        private TextView tv1;
        private TextView tv2;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layaut_iniciarsesion);
            txtusuario=(EditText)findViewById(R.id.txtusuario);
            txtclave=(EditText)findViewById(R.id.txtclave);
            tv1 = (TextView) findViewById(R.id.tv1);
            tv2 = (TextView) findViewById(R.id.tv2);


        }


        public void Guardar(View v) {

            File file;
            String usuario = txtusuario.getText().toString();
            String clave = txtclave.getText().toString();

            FileOutputStream fos;

            try {
                usuario = usuario + " ";
                file = getFilesDir();
                fos = openFileOutput("Code", Context.MODE_PRIVATE); //MODE PRIVATE
                fos.write(usuario.getBytes());
                fos.write(clave.getBytes());
                Toast.makeText(this, "Saved \n" + "Path --" + file + "\tCode.txt", Toast.LENGTH_LONG).show();
                txtusuario.setText("");
                txtclave.setText("");

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }


        public void Cargar(View v) {


            try {
                FileInputStream fileInputStream =  openFileInput("Code");
                int read = -1;
                StringBuffer buffer = new StringBuffer();
                while((read =fileInputStream.read())!= -1){
                    buffer.append((char)read);
                }

                String usuario = buffer.substring(0,buffer.indexOf(" "));
                String clave = buffer.substring(buffer.indexOf(" ")+1);
                tv1.setText(usuario);
                tv2.setText(clave);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this,"Loaded", Toast.LENGTH_SHORT).show();




        }

        public void Borrar (View v) {

            File dir = getFilesDir();
            File file = new File(dir, "Code");
            boolean deleted = file.delete();

        }

    }