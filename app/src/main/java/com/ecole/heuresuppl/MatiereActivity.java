package com.ecole.heuresuppl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ecole.heuresuppl.Matiere.List_MatiereActivity;
import com.ecole.heuresuppl.Model.Matiere;
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

public class MatiereActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matiere);

        Button retour_btn = (Button)findViewById(R.id.retour_btn);
        Button ajout_matiere_btn = (Button)findViewById(R.id.valider_matiere_btn);
        Button lister_matiere_nav = (Button)findViewById(R.id.lister_vh_nav);
        final TextInputEditText nummat_input = (TextInputEditText)findViewById(R.id.nummat_input);
        final TextInputEditText designation = (TextInputEditText)findViewById(R.id.designation);
        final TextInputEditText nbheure_input = (TextInputEditText)findViewById(R.id.nbheure_input);

        retour_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                mainActivity();

            }
        });

        ajout_matiere_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matiere mat = new Matiere();
                mat.setNummat(nummat_input.getText().toString());
                mat.setDesignation(designation.getText().toString());
                mat.setNbheure(parseInt(nbheure_input.getText().toString()));

                ajoutMatiere ajoutmatiere = new ajoutMatiere();
                ajoutmatiere.execute(mat);

                nummat_input.setText("");
                designation.setText("");
                nbheure_input.setText("");

            }
        });

        lister_matiere_nav.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listActivity();
            }
        });

    }

    public void mainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void listActivity(){
        Intent intent = new Intent(this, List_MatiereActivity.class);
        startActivity(intent);
    }

    private class ajoutMatiere extends AsyncTask<Matiere, Void, Void> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(Matiere... matieres) {

            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(matieres[0].toString());
                RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2/matiere")
                        .post(body)
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