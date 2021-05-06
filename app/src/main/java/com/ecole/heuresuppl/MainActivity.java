package com.ecole.heuresuppl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button professeur_btn = (Button)findViewById(R.id.professeur_btn);
        Button matiere_btn = (Button)findViewById(R.id.matiere_btn);
        Button volumehoraire = (Button)findViewById(R.id.volumehoraire_btn3);
        Button heureComp = (Button)findViewById(R.id.heureComp);

        professeur_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                professeurActivity();
            }

        });

        matiere_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                matiereActivity();
            }

        });

        volumehoraire.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                volumehoraireActivity();
            }

        });

        heureComp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    heurCompActivity();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void professeurActivity(){
        Intent intent = new Intent(this, professeur_activity.class);
        startActivity(intent);
    }

    public void matiereActivity(){
        Intent intent = new Intent(this, MatiereActivity.class);
        startActivity(intent);
    }

    public void volumehoraireActivity(){
        Intent intent = new Intent(this, VolumehoraireActivity.class);
        startActivity(intent);
    }

    public void heurCompActivity() throws ExecutionException, InterruptedException {
        Intent intent = new Intent(this, HeureCompActivity.class);
        HeureComple h = new HeureComple();
        String heurecomm = h.execute().get();
        intent.putExtra("heurecomp",heurecomm);
        startActivity(intent);
    }

    private class HeureComple extends AsyncTask<Void, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String ress = null;

        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/volumehoraire/")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ress = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ress;
        }
    }
}