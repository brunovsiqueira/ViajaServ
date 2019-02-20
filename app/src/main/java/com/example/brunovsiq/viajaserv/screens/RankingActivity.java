package com.example.brunovsiq.viajaserv.screens;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.brunovsiq.viajaserv.R;
import com.example.brunovsiq.viajaserv.models.Local;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private ListView listView;
    public static final String url = "http://35.231.198.240:3000/viagens/";
    private List<String> citiesList = new ArrayList<>();
    private ArrayList<Local> localArrayList = new ArrayList<>();
    private ProgressDialog pd;
    private String ano;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        listView = (ListView) findViewById(R.id.list);
        pd = ProgressDialog.show(RankingActivity.this, "Carregando", "Carregando viagens...");
        ano = getIntent().getStringExtra("ano");

        requestLocais();

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("locais.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private boolean verificaDuplicado(ArrayList<Local> localArrayList, Local local) {

        for (int i = 0; i < localArrayList.size(); i++) {
            if (localArrayList.get(i).getCidade().equalsIgnoreCase(local.getCidade())) {
                return true;
            }
        }
        return false;

    }


    private void requestLocais() {


        AndroidNetworking.get(url+ano)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //pd.dismiss();
                            JSONArray jsonArray = response;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //String string = (jsonObject.get("cidade").toString()) + " - Quantidade de viagens: " + (jsonObject.get("qtde").toString());
                                //citiesList.add(string);
                                Local local = new Local(jsonObject);
                                if (!verificaDuplicado(localArrayList, local)) {
                                    localArrayList.add(local);
                                }


                            }
                            //citiesList.sort();
                            Collections.sort(localArrayList);

                            for (int i = 0; i < localArrayList.size(); i++) {
                                String string = localArrayList.get(i).getCidade() + " - Quantidade de viagens: " + localArrayList.get(i).getQuantidade();
                                citiesList.add(string);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(RankingActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, citiesList);

                            // Assign adapter to ListView
                            listView.setAdapter(adapter);
                            pd.dismiss();
                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }
                        //pd.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            //pd.dismiss();
                            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String string = (jsonObject.get("cidade").toString()) + " - Quantidade de viagens: " + (jsonObject.get("qtde").toString());
                                citiesList.add(string);
                            }
                            Toast.makeText(RankingActivity.this, "Falha na conexão com o servidor", Toast.LENGTH_SHORT).show();
                            pd.dismiss();

                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }


                    }
                });






    }
}
