package com.ecole.heuresuppl.Volumehoraire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecole.heuresuppl.Model.Professeur;
import com.ecole.heuresuppl.Model.Volumehoraire;
import com.ecole.heuresuppl.Professeur.List_ProfesseurActivity;
import com.ecole.heuresuppl.R;
import com.ecole.heuresuppl.VolumehoraireActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

public class List_VolumehoraireActivity extends AppCompatActivity {

    private List<Volumehoraire> listV = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__volumehoraire);

        Button ajout_nav_vh = (Button)findViewById(R.id.ajout_nav_vh);
        LinearLayout list_volumeH_principal = (LinearLayout)findViewById(R.id.list_volumeH_principal);

        listVh listvh = new listVh();
        try {
            listV = listvh.execute().get();
            System.out.println(listV.size());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < listV.size(); i++){
            TextView profess = new TextView(this);
            profess.setTextSize(30);
            profess.setText(listV.get(i).getMatricule() + ", " + listV.get(i).getNummat());
            list_volumeH_principal.addView(profess);

            TextView taux = new TextView(this);
            taux.setTextSize(30);
            taux.setText(String.valueOf(listV.get(i).getVolumeHoraire()));
            list_volumeH_principal.addView(taux);

            Button modif_btn = new Button(this);
            modif_btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            modif_btn.setText("Modifier");

            final int finalI = i;
            modif_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    modif_vue(String.valueOf(listV.get(finalI).getId()));
                }
            });
            list_volumeH_principal.addView(modif_btn);

            Button supp_btn = new Button(this);
            supp_btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            supp_btn.setText("Supprimer");

            final int finalI1 = i;
            supp_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    suppAction(listV.get(finalI1).getId());
                }
            });
            list_volumeH_principal.addView(supp_btn);
        }

        ajout_nav_vh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                navajout();
            }
        });

    }

    public void modif_vue(String volh){
        Intent intent = new Intent(this,Modif_VolumehoraireActivity.class);
        intent.putExtra("vhoraire_id",volh);
        startActivity(intent);
    }

    public void suppAction(Integer id){
        suppVh suppvh = new suppVh();
        suppvh.execute(id);
        navajout();
    }

    public void navajout(){
        Intent intent = new Intent(this, VolumehoraireActivity.class);
        startActivity(intent);
    }

    private class listVh extends AsyncTask<Void, Void, List> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected List doInBackground(Void... voids) {
            List<Volumehoraire> listVh = new ArrayList<>();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/volumehoraire")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String resultat = response.body().string();
                JSONParser parser = new JSONParser();
                JSONArray array = (JSONArray)parser.parse(resultat);
                for(int i = 0; i < array.size(); i++){
                    JSONObject objet = (JSONObject)array.get(i);
                    Volumehoraire vh = new Volumehoraire(objet.get("matricule").toString(),objet.get("nummat").toString(),parseInt(objet.get("tauxhoraire").toString()));
                    vh.setId(parseInt(objet.get("id").toString()));
                    listVh.add(vh);
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            return listVh;
        }
    }

    private class suppVh extends AsyncTask<Integer, Void, Void>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(Integer... id) {

            Request request = new Request.Builder()
                    .url("http://10.0.2.2/volumehoraire/"+id[0])
                    .delete()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Success");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}