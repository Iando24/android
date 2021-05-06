package com.ecole.heuresuppl.Professeur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ecole.heuresuppl.Model.Professeur;
import com.ecole.heuresuppl.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Modification_ProfesseurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification__professeur);

        String prof = getIntent().getStringExtra("matricule_prof");
        JSONParser parser = new JSONParser();
        final TextInputEditText matriculeProf = (TextInputEditText) findViewById(R.id.matriculeProf);
        final TextInputEditText new_nom = (TextInputEditText)findViewById(R.id.new_nom);
        try {
            JSONObject json = (JSONObject) parser.parse(prof);
            matriculeProf.setText(json.get("matricule").toString());
            new_nom.setText(json.get("nom").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Button valider_btn = (Button)findViewById(R.id.valider_btn);

        valider_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                update up = new update();
                up.execute(String.valueOf(matriculeProf.getText()),String.valueOf(new_nom.getText()));
                listDirection();
            }
        });

        Button retour_btn = (Button)findViewById(R.id.retour_btn);

        retour_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDirection();
            }
        });

    }

    public void listDirection(){
        Intent intent = new Intent(this,List_ProfesseurActivity.class);
        startActivity(intent);
    }

    private class update extends AsyncTask<String, Void, Void>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... data) {

            String prof_insert = "{\"nom\" : \""+data[1]+"\"}";
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(prof_insert);
                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2/professeur/"+data[0])
                        .put(body)
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                System.out.println("Success");
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}