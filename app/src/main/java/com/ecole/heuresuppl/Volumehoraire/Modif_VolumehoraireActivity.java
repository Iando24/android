package com.ecole.heuresuppl.Volumehoraire;

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

import com.ecole.heuresuppl.Model.Volumehoraire;
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

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

public class Modif_VolumehoraireActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif__volumehoraire);

        final String vol = getIntent().getStringExtra("vhoraire_id");
        JSONParser parser = new JSONParser();
        final Spinner professeur_list = (Spinner)findViewById(R.id.professeur_list);
        final Spinner matiere_list = (Spinner)findViewById(R.id.matiere_list);
        final EditText tauxhoraire = (EditText)findViewById(R.id.tauxhoraire);

        professeur_list.setOnItemSelectedListener(this);
        matiere_list.setOnItemSelectedListener(this);

        getNomProfesseur nom = new getNomProfesseur();
        getAllMatiere matt = new getAllMatiere();

        List<String> matricule = new ArrayList<>();
        List<String> nummat = new ArrayList<>();

        Button valider_btn = (Button)findViewById(R.id.valider_btn);

        try {
            matricule = nom.execute().get();
            nummat = matt.execute().get();
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, matricule);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nummat);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            professeur_list.setAdapter(dataAdapter1);
            matiere_list.setAdapter(dataAdapter2);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        valider_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matricu = String.valueOf(professeur_list.getSelectedItem());
                String mat_n = String.valueOf(matiere_list.getSelectedItem());
                int th = parseInt(String.valueOf(tauxhoraire.getText()));
                Volumehoraire vh = new Volumehoraire(matricu, mat_n, th);
                vh.setId(parseInt(vol));
                update ph = new update();
                ph.execute(vh);
                navlist();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void navlist(){
        Intent intent = new Intent(this, List_VolumehoraireActivity.class);
        startActivity(intent);
    }

    private class getNomProfesseur extends AsyncTask<Void, Void, List<String>> {
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

    private class update extends AsyncTask<Volumehoraire, Void, Void>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(Volumehoraire... vhs) {
            String up = "{\"matricule\" : \""+vhs[0].getMatricule()+"\",\"nummat\" : \""+vhs[0].getNummat()+"\",\"tauxhoraire\" : "+vhs[0].getVolumeHoraire()+"}";

            JSONParser parser = new JSONParser();
            JSONObject json = null;

            try {
                json = (JSONObject) parser.parse(up);
                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2/volumehoraire/"+vhs[0].getId())
                        .put(body)
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                System.out.println("Success");
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}