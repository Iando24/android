package com.ecole.heuresuppl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.Comparator;

import static java.lang.Integer.parseInt;

public class HeureCompActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heure_comp);

        Button retour_btn = (Button) findViewById(R.id.retour_btn);
        retour_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retour_nav();
            }
        });

        String heurecomp = getIntent().getStringExtra("heurecomp");
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        TableLayout table = new TableLayout(this);

        JSONParser parser = new JSONParser();
        JSONArray array = null;
        JSONArray array1 = null;
        try {
            array = (JSONArray)parser.parse(heurecomp);
            array1 = (JSONArray)parser.parse(heurecomp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TableRow title = new TableRow(this);

        TextView des = new TextView(this);
        TextView taux = new TextView(this);
        TextView nbheure = new TextView(this);

        des.setText(" " + "Matricule");
        taux.setText(" " + "Nom");
        nbheure.setText(" " + "Montant");

        title.addView(des);
        title.addView(taux);
        title.addView(nbheure);

        table.addView(title);

        int j = 0;
        while(!array.isEmpty()) {

            JSONObject json = (JSONObject) array.get(j);
            int t = parseInt(json.get("nbheure").toString()) * parseInt(json.get("tauxhoraire").toString());

            TableRow conte = new TableRow(this);
            TextView matricule = new TextView(this);
            TextView nom = new TextView(this);
            TextView montant = new TextView(this);

            array.remove(j);
            if (array.size() != 0){
                for (int k = 0; k < array.size(); k++) {
                    JSONObject json2 = (JSONObject) array.get(k);
                    JSONComparator comparator = new JSONComparator();
                    if (comparator.compare(json, json2) == 0) {
                        t = (parseInt(json2.get("nbheure").toString()) * parseInt(json2.get("tauxhoraire").toString())) + t;
                        matricule.setText(String.valueOf(json.get("matricule")));
                        nom.setText(String.valueOf(json.get("nom")));
                        montant.setText(String.valueOf(t));
                        array.remove(k);
                    } else {
                        matricule.setText(String.valueOf(json.get("matricule")));
                        nom.setText(String.valueOf(json.get("nom")));
                        montant.setText(String.valueOf(t));
                    }
                }
            }else{
                matricule.setText(String.valueOf(json.get("matricule")));
                nom.setText(String.valueOf(json.get("nom")));
                montant.setText(String.valueOf(t));
            }
            conte.addView(matricule);
            conte.addView(nom);
            conte.addView(montant);

            table.addView(conte);
            System.out.println(t);
        }

        int i = 0;
        int total = 0;

        while(array1.size() != 0){
            JSONObject objet = (JSONObject)array1.get(i);
            total += parseInt(String.valueOf(objet.get("tauxhoraire"))) * parseInt(String.valueOf(objet.get("nbheure")));
            array1.remove(i);
        }

        TableRow conte = new TableRow(this);

        TextView matricule = new TextView(this);
        TextView nom = new TextView(this);
        TextView montant = new TextView(this);

        matricule.setText("");
        nom.setText("Total montant: ");
        montant.setText(String.valueOf(total));

        conte.addView(matricule);
        conte.addView(nom);
        conte.addView(montant);

        table.addView(conte);

        mainLayout.addView(table);
    }

    private class JSONComparator implements Comparator<JSONObject>{

        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            String val1 = (String)o1.get("matricule");
            String val2 = (String)o2.get("matricule");
            return val1.compareTo(val2);
        }
    }

    public void retour_nav(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}