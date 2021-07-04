package com.example.fit5046_assignment3.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.fit5046_assignment3.Entity.Record;

public class SharedViewModel extends ViewModel {
    private double longAltitude;
    private double latAltitude;
    private Record record;
    private String temperature;
    private String humidity;
    private String pressure;
    private Record editRecord;
    private double[][] cor;

    public SharedViewModel() {
        this.latAltitude = -37.876823;
        this.longAltitude = 145.045837;
    }

    public double[][] getCor() {
        return cor;
    }

    public void setCor(double[][] cor) {
        this.cor = cor;
    }

    public double getLongAltitude() {
        return longAltitude;
    }

    public double getLatAltitude() {
        return latAltitude;
    }

    public void setLongAltitude(double longAltitude) {
        this.longAltitude = longAltitude;
    }

    public void setLatAltitude(double latAltitude) {
        this.latAltitude = latAltitude;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public Record getEditRecord() {
        return editRecord;
    }

    public void setEditRecord(Record editRecord) {
        this.editRecord = editRecord;
    }
}
