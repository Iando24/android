package com.ecole.heuresuppl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ecole.heuresuppl.Model.Volumehoraire;
import com.ecole.heuresuppl.Professeur.List_ProfesseurActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

public class BulletinDePaieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_de_paie);

        String matri = getIntent().getStringExtra("matricule");
        TextView matricule = (TextView)findViewById(R.id.matricule);
        TextView nom = (TextView)findViewById(R.id.nom);
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        TableLayout table = new TableLayout(this);

        JSONParser parser = new JSONParser();
        try {
            JSONArray array = (JSONArray)parser.parse(matri);
            JSONObject n = (JSONObject)array.get(0);
            JSONObject m = (JSONObject)array.get(0);
            nom.setText(n.get("nom").toString());
            matricule.setText(m.get("matricule").toString());

            TableRow title = new TableRow(this);

            TextView des = new TextView(this);
            TextView taux = new TextView(this);
            TextView nbheure = new TextView(this);
            TextView montant = new TextView(this);

            des.setText(" " + "Désignation");
            taux.setText(" " + "Taux");
            nbheure.setText(" " + "Nbheure");
            montant.setText(" " + "Montant");

            title.addView(des);
            title.addView(taux);
            title.addView(nbheure);
            title.addView(montant);

            table.addView(title);

            for (int i=0; i < array.size(); i++) {

                JSONObject cont = (JSONObject)array.get(i);

                TableRow row = new TableRow(this);

                TextView case1 = new TextView(this);
                case1.setText(" " + cont.get("designation").toString());
                row.addView(case1);

                TextView case2 = new TextView(this);
                case2.setText(" " + cont.get("tauxhoraire").toString());
                row.addView(case2);

                TextView case3 = new TextView(this);
                case3.setText(" " + cont.get("nbheure").toString());
                row.addView(case3);

                TextView case4 = new TextView(this);
                case4.setText(" " + String.valueOf(parseInt(cont.get("tauxhoraire").toString()) * parseInt(cont.get("nbheure").toString())));
                row.addView(case4);

                table.addView(row);
            }
            int total = 0;

            for(int i = 0; i < array.size(); i++){
                JSONObject cont = (JSONObject)array.get(i);

                if(i < array.size() - 1){
                    total += parseInt(cont.get("tauxhoraire").toString()) * parseInt(cont.get("nbheure").toString());
                }else{
                    total += parseInt(cont.get("tauxhoraire").toString()) * parseInt(cont.get("nbheure").toString());

                    TableRow row = new TableRow(this);

                    TextView case1 = new TextView(this);
                    case1.setText("");
                    row.addView(case1);

                    TextView case2 = new TextView(this);
                    case2.setText("");
                    row.addView(case2);

                    TextView case3 = new TextView(this);
                    case3.setText(" " + "Total: ");
                    row.addView(case3);

                    TextView case4 = new TextView(this);
                    case4.setText(String.valueOf(total));
                    row.addView(case4);

                    table.addView(row);
                }
            }

            mainLayout.addView(table);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        Button retour_btn = (Button)findViewById(R.id.retour_btn);

        retour_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listProf_nav();
            }
        });

    }

    public void listProf_nav(){
        Intent intent = new Intent(this, List_ProfesseurActivity.class);
        startActivity(intent);
    }
}