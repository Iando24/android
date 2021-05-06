package com.ecole.heuresuppl.Matiere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecole.heuresuppl.MatiereActivity;
import com.ecole.heuresuppl.Model.Matiere;
import com.ecole.heuresuppl.R;

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

public class List_MatiereActivity extends AppCompatActivity {

    private List<Matiere> listM = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__matiere);

        Button ajout_nav_matiere = (Button)findViewById(R.id.ajout_nav_vh);
        ajout_nav_matiere.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                nav_ajout();
            }
        });

        LinearLayout list_mat_principal = (LinearLayout)findViewById(R.id.list_mat_principal);
        getAllMatiere getallmatiere = new getAllMatiere();

        try {
            listM = getallmatiere.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < listM.size(); i++){
            TextView apropos = new TextView(this);
            apropos.setTextSize(30);
            apropos.setText(listM.get(i).getNummat() + ", " +listM.get(i).getDesignation() + ", "+ listM.get(i).getNbheure());
            list_mat_principal.addView(apropos);

            Button modif_mat_btn = new Button(this);
            modif_mat_btn.setText("Modifier");
            final int finalI = i;
            modif_mat_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Matiere mt_md = new Matiere(listM.get(finalI).getNummat(),listM.get(finalI).getDesignation(),parseInt(String.valueOf(listM.get(finalI).getNbheure())));
                    modif_btn_action(mt_md.toString());
                }
            });
            list_mat_principal.addView(modif_mat_btn);

            Button supp_mat_btn = new Button(this);
            supp_mat_btn.setText("Supprimer");
            supp_mat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    suppAction(listM.get(finalI).getNummat());
                }
            });
            list_mat_principal.addView(supp_mat_btn);

        }
    }

    public void nav_ajout(){
        Intent intent = new Intent(this, MatiereActivity.class);
        startActivity(intent);
    }

    public void modif_btn_action(String m){
        Intent intent = new Intent(this, Modif_Matiere.class);
        intent.putExtra("matiere", m);
        startActivity(intent);
    }

    public void suppAction(String m){
        suppMatClass sup = new suppMatClass();
        sup.execute(m);
    }

    private class getAllMatiere extends AsyncTask<Void, Void, List>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected List doInBackground(Void... voids) {

            Request request = new Request.Builder()
                    .url("http://10.0.2.2/matiere")
                    .build();
            Response response = null;
            List<Matiere> listmatiere = new ArrayList<>();

            try {
                response = client.newCall(request).execute();
                String resu = response.body().string();
                JSONParser parser = new JSONParser();
                JSONArray json = (JSONArray) parser.parse(resu);

                for(int i = 0; i < json.size(); i++){
                    JSONObject m = (JSONObject) json.get(i);
                    Matiere matiee = new Matiere(m.get("nummat").toString(),m.get("designation").toString(),parseInt(m.get("nbheure").toString()));
                    listmatiere.add(matiee);
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            return listmatiere;
        }
    }

    private class suppMatClass extends AsyncTask<String, Void, Void>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(String... strings) {
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/matiere/"+strings[0])
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

    public void reload(){
        Intent intent = new Intent(this, MatiereActivity.class);
        startActivity(intent);
    }
}