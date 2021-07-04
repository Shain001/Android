package com.example.fit5046_assignment3.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(primaryKeys = {"date", "userEmail"})
@TypeConverters(DateConverter.class)
public class Record {
    @NonNull
    @ColumnInfo(name = "date")
    private final Date date;
    @NonNull
    @ColumnInfo(name = "userEmail")
    private String userEmail;

    @ColumnInfo(name = "pain_level")
    private int painLevel;
    @NonNull
    @ColumnInfo(name = "mood_level")
    private int moodLevel;
    @NonNull
    @ColumnInfo(name = "pain_location")
    private String painLocation;

    @ColumnInfo(name = "step_goal")
    private long stepGoal;

    @ColumnInfo(name = "step_actual")
    private long stepActual;
    @NonNull
    @ColumnInfo(name = "time_reminder")
    private Date timeReminder;

    @ColumnInfo(name = "temperature")
    private double temperature;

    @ColumnInfo(name = "humidity")
    private double humidity;

    @ColumnInfo(name = "pressure")
    private double pressure;

    public Record(Date date, @NonNull String userEmail, int painLevel, int moodLevel, @NonNull String painLocation, long stepGoal, long stepActual, Date timeReminder
    , double temperature, double humidity, double pressure) {
        this.date = date;
        this.userEmail = userEmail;
        this.painLevel = painLevel;
        this.moodLevel = moodLevel;
        this.painLocation = painLocation;
        this.stepGoal = stepGoal;
        this.stepActual = stepActual;
        this.timeReminder = timeReminder;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }


    public Date getDate() {
        return date;
    }

    @NonNull
    public String getUserEmail() {
        return userEmail;
    }

    public int getPainLevel() {
        return painLevel;
    }

    public int getMoodLevel() {
        return moodLevel;
    }

    @NonNull
    public String getPainLocation() {
        return painLocation;
    }

    public long getStepGoal() {
        return stepGoal;
    }

    public long getStepActual() {
        return stepActual;
    }

    public Date getTimeReminder() {
        return timeReminder;
    }

    public void setUserEmail(@NonNull String userEmail) {
        this.userEmail = userEmail;
    }

    public void setPainLevel(int painLevel) {
        this.painLevel = painLevel;
    }

    public void setMoodLevel(int moodLevel) {
        this.moodLevel = moodLevel;
    }

    public void setPainLocation(@NonNull String painLocation) {
        this.painLocation = painLocation;
    }

    public void setStepGoal(long stepGoal) {
        this.stepGoal = stepGoal;
    }

    public void setStepActual(long stepActual) {
        this.stepActual = stepActual;
    }

    public void setTimeReminder(@NonNull Date timeReminder) {
        this.timeReminder = timeReminder;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public float getFactor(String factor){
        double result = 0;
        switch (factor){
            case "humidity": result = humidity;
            case "temperature": result = temperature;
            case "pressure": result = pressure;
            default: break;
        }
        return (float)result;
    }
}
