package it.redhat.mrt.model;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Report {
    public String name;
    public String email;
    public String costCenter;
    public String rhid;
    public String carId;
    public double mileageRate;
    public List<Trip> trips;
    public int year;
    public int month;

    public String getPeriod() {
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + year;
    }

    public int getTotalDistance(){
        return Trip.getTotalMileage(rhid, year, month);
    }

    public int getMonthlyDistance(){
        int monthlyDistance = 0;
        for (Trip trip : trips) {
            monthlyDistance += trip.distance;
        }
        return monthlyDistance;
    }

    public static Report build(String userid) {

        Associate associate = Associate.findByUserid(userid);
        Report report = new Report();
        
        report.rhid = associate.rhid;
        report.name = associate.fullName;
        report.email = associate.userid + "@redhat.com";
        report.costCenter = associate.costCenter;
        report.carId = associate.carid;
        report.mileageRate = associate.mileageRate;

        return report;
    }

}
