package com.ecole.heuresuppl.Professeur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecole.heuresuppl.BulletinDePaieActivity;
import com.ecole.heuresuppl.Model.Professeur;
import com.ecole.heuresuppl.R;
import com.ecole.heuresuppl.professeur_activity;

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

public class List_ProfesseurActivity extends AppCompatActivity {

    private List<Professeur> listP = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__professeur);

        Button ajout_nav_prof_btn = (Button)findViewById(R.id.ajout_nav_prof_btn);
        LinearLayout list_prof_principal = (LinearLayout)findViewById(R.id.list_mat_principal);

        afficheListProf profController = new afficheListProf();
        try {
            listP = profController.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < listP.size(); i++){

            TextView profess = new TextView(this);
            profess.setTextSize(30);
            profess.setText(listP.get(i).getMatricule() + ", " + listP.get(i).getNom());
            list_prof_principal.addView(profess);

            LinearLayout lh = new LinearLayout(this);
            lh.setOrientation(LinearLayout.VERTICAL);

            Button modif_btn = new Button(this);
            modif_btn.setText("Modifier");

            final int finalI = i;
            modif_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Professeur pr_md = new Professeur(listP.get(finalI).getMatricule(),listP.get(finalI).getNom());
                    modif_vue(pr_md.toString());
                }
            });

            lh.addView(modif_btn);

            Button supp_btn = new Button(this);
            supp_btn.setText("Supprimer");

            supp_btn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    suppAction(listP.get(finalI).getMatricule());
                }
            });

            lh.addView(supp_btn);

            final Button bulletinPaie = new Button(this);
            bulletinPaie.setText("Bulletin de paie");
            bulletinPaie.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    BulletinPaie bp = new BulletinPaie();
                    try {
                        String resultat = bp.execute(listP.get(finalI).getMatricule()).get();
                        bulletinPaieNav(resultat);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            lh.addView(bulletinPaie);

            list_prof_principal.addView(lh);

        }

        ajout_nav_prof_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                professeurActivity();
            }
        });
    }

    public void modif_vue(String matricule_prof_modif){
        Intent intent = new Intent(this, Modification_ProfesseurActivity.class);
        intent.putExtra("matricule_prof",matricule_prof_modif);
        startActivity(intent);
    }

    public void suppAction(String matricule_prof_supp){
        suppProfClass sup = new suppProfClass();
        sup.execute(matricule_prof_supp);
    }

    public void bulletinPaieNav(String matricule_prof){
        Intent intent = new Intent(this, BulletinDePaieActivity.class);
        intent.putExtra("matricule",matricule_prof);
        startActivity(intent);
    }

    public void professeurActivity(){
        Intent intent = new Intent(this, professeur_activity.class);
        startActivity(intent);
    }

    private class afficheListProf extends AsyncTask<Void, Void, List> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected List doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/professeur")
                    .build();
            Response response = null;
            List<Professeur> listprof = new ArrayList<>();

            try {
                response = client.newCall(request).execute();
                String resu = response.body().string();
                JSONParser parser = new JSONParser();
                JSONArray json = (JSONArray) parser.parse(resu);

                for(int i = 0; i < json.size(); i++){
                    JSONObject p = (JSONObject) json.get(i);
                    Professeur pro = new Professeur(p.get("matricule").toString(),p.get("nom").toString());
                    listprof.add(pro);
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            return listprof;
        }
    }

    private class suppProfClass extends AsyncTask<String, Void, Void>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... matricule_prof_supp) {
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/professeur/"+matricule_prof_supp[0])
                    .delete()
                    .build();

            try(Response response = client.newCall(request).execute()){
                reload();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class BulletinPaie extends AsyncTask<String, Void, String>{
        OkHttpClient client = new OkHttpClient();
        String ress = null;

        @Override
        protected String doInBackground(String... strings) {
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/bulletindepaie/"+strings[0])
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

    public void reload(){
        Intent intent = new Intent(this,professeur_activity.class);
        startActivity(intent);
    }
}