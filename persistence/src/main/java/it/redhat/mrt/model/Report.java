package it.redhat.mrt.model;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Report {
    public String name;
    public String rhid;
    public String carModel;
    public double mileageRate;
    public List<Trip> trips;
    public int year;
    public int month;

    public String getPeriod() {
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + year;
    }

    public static Report build(String userid, int year, int month) {

        Associate associate = Associate.findByUserid(userid);
        Report report = new Report();
        
        report.rhid = associate.rhid;
        report.name = associate.fullName;
        report.carModel = associate.carModel;
        report.mileageRate = associate.mileageRate;        
        report.year = year;
        report.month = month;

        return report;
    }

}
