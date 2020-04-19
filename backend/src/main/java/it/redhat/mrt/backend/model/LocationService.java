package it.redhat.mrt.backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;

import org.bson.Document;

import io.quarkus.mongodb.runtime.MongoClientName;

@ApplicationScoped
public class LocationService {
    @MongoClientName("mrt")
    @Inject MongoClient mongoClient;

    public List<Location> list(){
        List<Location> list = new ArrayList<>();
        MongoCursor<Document> cursor = mongoClient.getDatabase("mrt").getCollection("locations").find().iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Location location = new Location().build(document);
                list.add(location);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public void add(Location location){
        Document document = location.toDocument();
        if(document != null){
            mongoClient.getDatabase("mrt").getCollection("locations").insertOne(document);
        }
    }

}