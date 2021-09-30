package it.redhat.mrt.model;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.eclipse.microprofile.jwt.JsonWebToken;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

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

    //private static final Logger logger = LoggerFactory.getLogger("it.redhat.mrt");

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

    public static Report build(JsonWebToken idToken) {

        Report report = new Report();
        
        if(idToken != null){
            Object userGivenName = idToken.getClaim("given_name");
            Object userFamilyName = idToken.getClaim("family_name");
            if ((userGivenName != null) && (userFamilyName != null)){
                report.name = userGivenName.toString() + " " + userFamilyName.toString();
            }

            Object emailObject = idToken.getClaim("email");
            if (emailObject != null) {
                report.email = emailObject.toString();
            }    

            Object ccObject = idToken.getClaim("cost_center");
            if (ccObject != null) {
                report.costCenter = ccObject.toString();
            }    

            Object rhidObject = idToken.getClaim("rhid");
            if (rhidObject != null) {
                report.rhid = rhidObject.toString();
            }    

            Object caridObject = idToken.getClaim("carid");
            if (caridObject != null) {
                report.carId = caridObject.toString();
            }    

            Object mrObject = idToken.getClaim("mileage_rate");
            if (mrObject != null) {
                report.mileageRate = Double.parseDouble(mrObject.toString());
            }    
        }

        return report;
    }

}
