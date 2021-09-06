package it.redhat.mrt.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="locations")
public class Location  extends PanacheMongoEntity {
    public String destination;
    public int distance;

    public static Location findByName(String name){
        return find("name", name).firstResult();
    }
}
