package it.redhat.mrt.pdf.model;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Report {
    private Associate associate;
    private List<Trip> trips;
    private int year;
    private int month;

    public Report setYear(int year){
        this.year = year;
        return this;
    }
 
    public int getYear(){
        return this.year;
    }
 
    public Report setMonth(int month){
        this.month = month;
        return this;
    }
 
    public int getMonth(){
        return this.month;
    }
 
    public Report setAssociate(Associate associate){
        this.associate = associate;
        return this;
    }
 
    public Associate getAssociate(){
        return this.associate;
    }
 
    public Report setTrips(List<Trip> trips){
        this.trips = trips;
        return this;
    }
 
    public List<Trip> getTrips(){
        return this.trips;
    }
 
    public String getPeriod() {
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + year;
    }

    public int getMonthlyDistance(){
        int monthlyDistance = 0;
        for (Trip trip : trips) {
            monthlyDistance += trip.getLocation().getDistance();
        }
        return monthlyDistance;
    }
 
}