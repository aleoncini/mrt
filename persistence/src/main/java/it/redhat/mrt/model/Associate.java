package it.redhat.mrt.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="associates")
public class Associate extends PanacheMongoEntity {
    
    public String userid; 
    public String rhid;
    public String fullName;
    public String carModel;
    public double mileageRate;

    public static Associate findByUserid(String userid){
        return Associate.find("userid = ?1", userid).firstResult();
    }

}