package com.ecole.heuresuppl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecole.heuresuppl.Model.Professeur;
import com.ecole.heuresuppl.Model.Volumehoraire;
import com.ecole.heuresuppl.Volumehoraire.List_VolumehoraireActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

public class VolumehoraireActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volumehoraire);

        Button retour_btn = (Button)findViewById(R.id.retour_btn);
        Button lister_vh_nav = (Button)findViewById(R.id.lister_vh_nav);
        Button ajouter = (Button)findViewById(R.id.ajouter);

        final Spinner professeur_list = (Spinner)findViewById(R.id.professeur_list);
        final Spinner matiere_list = (Spinner)findViewById(R.id.matiere_list);
        final EditText tauxhoraire = (EditText)findViewById(R.id.tauxhoraire);

        professeur_list.setOnItemSelectedListener(this);
        matiere_list.setOnItemSelectedListener(this);

        getNomProfesseur nom = new getNomProfesseur();
        getAllMatiere matt = new getAllMatiere();

        List<String> matricule = new ArrayList<>();
        List<String> nummat = new ArrayList<>();
        try {
            matricule = nom.execute().get();
            nummat = matt.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, matricule);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nummat);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        professeur_list.setAdapter(dataAdapter1);
        matiere_list.setAdapter(dataAdapter2);

        retour_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    mainActivity();

                }
        });

        lister_vh_nav.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list_vh_activity();
            }
        });

        ajouter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String matricu = String.valueOf(professeur_list.getSelectedItem());
                String mat_n = String.valueOf(matiere_list.getSelectedItem());
                int th = parseInt(String.valueOf(tauxhoraire.getText()));
                Volumehoraire vh = new Volumehoraire(matricu, mat_n, th);
                postVolumehoraire ph = new postVolumehoraire();
                ph.execute(vh);
            }
        });

    }

    public void mainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void list_vh_activity(){
        Intent intent = new Intent(this, List_VolumehoraireActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class getNomProfesseur extends AsyncTask<Void, Void, List<String>>{
        OkHttpClient client = new OkHttpClient();
        @Override
        protected List doInBackground(Void... voids) {

            Request request = new Request.Builder()
                    .url("http://10.0.2.2/professeur")
                    .get()
                    .build();
            Response response = null;
            List<String> listmatricule = new ArrayList<>();

            try {
                response = client.newCall(request).execute();
                String resu = response.body().string();
                JSONParser parser = new JSONParser();
                JSONArray json = (JSONArray) parser.parse(resu);

                for(int i = 0; i < json.size(); i++){
                    JSONObject p = (JSONObject) json.get(i);
                    String matricule = p.get("matricule").toString();
                    listmatricule.add(matricule);
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            return listmatricule;
        }
    }

    private class getAllMatiere extends AsyncTask<Void, Void, List>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected List doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/matiere")
                    .get()
                    .build();
            Response response = null;
            List<String> listnummat = new ArrayList<>();

            try {
                response = client.newCall(request).execute();
                String resu = response.body().string();
                JSONParser parser = new JSONParser();
                JSONArray json = (JSONArray) parser.parse(resu);

                for(int i = 0; i < json.size(); i++){
                    JSONObject p = (JSONObject) json.get(i);
                    String nummat = p.get("nummat").toString();
                    listnummat.add(nummat);
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            return listnummat;
        }
    }

    private class postVolumehoraire extends AsyncTask<Volumehoraire, Void, Void>{
        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(Volumehoraire... vh) {
            String voh_insert = "{\"matricule\" : \""+vh[0].getMatricule()+"\",\"nummat\" : \""+vh[0].getNummat()+"\",\"tauxhoraire\" : "+vh[0].getVolumeHoraire()+"}";

            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(voh_insert);
                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2/volumehoraire")
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                System.out.println(response);
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}