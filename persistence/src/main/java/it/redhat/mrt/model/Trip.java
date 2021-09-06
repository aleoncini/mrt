package it.redhat.mrt.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="trips")
public class Trip extends PanacheMongoEntity {
    
    public String rhid;
    public DateOfTrip date;
    public Location location;
    public String purpose;

}
