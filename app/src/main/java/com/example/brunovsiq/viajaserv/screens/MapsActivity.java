package com.example.brunovsiq.viajaserv.screens;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.brunovsiq.viajaserv.R;
import com.example.brunovsiq.viajaserv.models.Local;
import com.example.brunovsiq.viajaserv.models.MyItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Local> localArrayList = new ArrayList<>();
    private ClusterManager<MyItem> mClusterManager;
    public static final String url = "http://35.231.198.240:3000/viagens/"; //adicionar ano
    private String ano = "2018";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pd = ProgressDialog.show(MapsActivity.this, "Carregando", "Carregando viagens...");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ano = getIntent().getStringExtra("ano");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        requestLocais();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-15.7801, -47.9292)));
    }

    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        //addItems();
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

    public void loadMarkers() {
        setUpClusterer();
        pd.dismiss();

        for (Local local : localArrayList) {
            mClusterManager.addItem(new MyItem(local.getLatitude(), local.getLongitude(), local.getCidade() + " - " + local.getEstado(), "Quantidade de viagens: " + local.getQuantidade()));
//            mMap.addMarker(new MarkerOptions().position(new LatLng(local.getLatitude(), local.getLongitude()))
//                    .title(local.getCidade() + " - " + local.getEstado())
//                    .snippet("Quantidade de viagens: " + local.getQuantidade()));
        }
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


        AndroidNetworking.get(url + ano)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //pd.dismiss();
                            JSONArray jsonArray = response;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Local local = new Local(jsonObject);
                                if (!verificaDuplicado(localArrayList, local)) {
                                    localArrayList.add(local);
                                }
                            }
                            loadMarkers();
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
                                Local local = new Local(jsonObject);
                                localArrayList.add(local);
                            }
                            loadMarkers();
                            Toast.makeText(MapsActivity.this, "Falha na conexão com o servidor", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }


                    }
                });






    }
}
