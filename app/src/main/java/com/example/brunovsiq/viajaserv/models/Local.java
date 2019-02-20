package com.example.brunovsiq.viajaserv.models;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Local implements Comparable<Local> {

    private String estado;
    private String cidade;
    private int quantidade;
    private double latitude;
    private double longitude;

    public Local(JSONObject jsonObject) {
        try {

            this.estado = jsonObject.getString("estado");
            this.cidade = jsonObject.getString("cidade");
            this.quantidade = Integer.parseInt(jsonObject.getString("qtde"));
            this.latitude = Double.parseDouble(jsonObject.getString("latitude"));
            this.longitude = Double.parseDouble(jsonObject.getString("longitude"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public int compareTo(Local o) {
        return o.quantidade - quantidade;
    }
}
