package it.redhat.mrt.model;

import java.util.List;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="trips")
public class Trip extends PanacheMongoEntity {
    
    public String rhid;
    public int year;
    public int month;
    public int day;
    public int distance;
    public String destination;
    public String purpose;

    public static List<Trip> getTrips(String rhid, int year, int month){
        return Trip.find("rhid = ?1 and year = ?2 and month = ?3", rhid, year, month).list();
    }

}