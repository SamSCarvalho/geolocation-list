package com.example.geolocationlist;

public class PositionModel {
    double latitude, longitude;
    String dataHora;

    public PositionModel(double latitude, double longitude, String dataHora) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataHora = dataHora;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
