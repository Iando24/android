package com.ecole.heuresuppl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ecole.heuresuppl.Model.Professeur;
import com.ecole.heuresuppl.Professeur.List_ProfesseurActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class professeur_activity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professeur_activity);

        Button retour_btn = (Button)findViewById(R.id.retour_btn);
        Button liste_prof_btn = (Button)findViewById(R.id.liste_prof_btn);
        final TextView debug_resultat = (TextView) findViewById(R.id.debug_resultat);
        final TextInputEditText matricule_input = (TextInputEditText)findViewById(R.id.matricule_input);
        final TextInputEditText nom_input = (TextInputEditText)findViewById(R.id.nom_input);
        Button ajout_prof_btn = (Button)findViewById(R.id.ajout_prof_btn);

        ajout_prof_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Professeur pr = new Professeur();
                pr.setMatricule(matricule_input.getText().toString());
                pr.setNom(nom_input.getText().toString());

                ajoutAction(pr);
                matricule_input.setText("");
                nom_input.setText("");

                debug_resultat.setText(pr.toString());
            }
        });
        retour_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mainActivity();
            }
        });

        liste_prof_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listProfActivity();
            }
        });
    }

    public void mainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void listProfActivity(){
        Intent intent = new Intent(this, List_ProfesseurActivity.class);
        startActivity(intent);
    }

    public void ajoutAction(Professeur p){
        ajoutProff aj = new ajoutProff();
        aj.execute(p);
    }

    private class ajoutProff extends AsyncTask<Professeur, Void, Void>{
        OkHttpClient client = new OkHttpClient();
        @Override
        protected Void doInBackground(Professeur... professeurs) {
            String prof_insert = "{\"matricule\" : \""+professeurs[0].getMatricule()+"\",\"nom\" : \""+professeurs[0].getNom()+"\"}";
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(prof_insert);

                System.out.println(json);

                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2/professeur")
                        .post(body)
                        .build();

                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    System.out.println("Success");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}