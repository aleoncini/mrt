package it.redhat.mrt.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="associates")
public class Associate extends PanacheMongoEntity {
    
    public String name;
    public String email;
    public String costCenter;
    public String rhid;
    public String carId;
    public double mileageRate;

    public static Associate findByRhid(String rhid){
        return find("rhid", rhid).firstResult();
    }

}
