package com.ecole.heuresuppl.Matiere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ecole.heuresuppl.Professeur.List_ProfesseurActivity;
import com.ecole.heuresuppl.Professeur.Modification_ProfesseurActivity;
import com.ecole.heuresuppl.R;
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

import static java.lang.Integer.parseInt;

public class Modif_Matiere extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif__matiere);

        String mat = getIntent().getStringExtra("matiere");
        JSONParser parser = new JSONParser();

        final TextInputEditText nummat_input = (TextInputEditText)findViewById(R.id.nummat_input);
        final TextInputEditText designation = (TextInputEditText)findViewById(R.id.designation);
        final TextInputEditText nbheure_input = (TextInputEditText)findViewById(R.id.nbheure_input);

        try {
            JSONObject json = (JSONObject) parser.parse(mat);
            nummat_input.setText(json.get("nummat").toString());
            designation.setText(json.get("designation").toString());
            nbheure_input.setText(json.get("nbheure").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Button valider_matiere_btn = (Button)findViewById(R.id.valider_matiere_btn);

        valider_matiere_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                update up = new update();
                up.execute(String.valueOf(nummat_input.getText()),String.valueOf(designation.getText()),String.valueOf(nbheure_input.getText()));
                listDirection();
            }
        });

    }

    public void listDirection(){
        Intent intent = new Intent(this, List_MatiereActivity.class);
        startActivity(intent);
    }

    private class update extends AsyncTask<String, Void, Void> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... data) {

            String matiere_info = "{\"designation\" : \""+data[1]+"\",\"nbheure\""+parseInt(data[2])+"}";
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(matiere_info);
                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2/matiere/"+data[0])
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