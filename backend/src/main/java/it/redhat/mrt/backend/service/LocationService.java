package it.redhat.mrt.backend.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;

import io.quarkus.mongodb.runtime.MongoClientName;
import it.redhat.mrt.backend.model.Location;
import it.redhat.mrt.backend.model.serializer.LocationSerializer;

@ApplicationScoped
public class LocationService {
    @MongoClientName("mrt")
    @Inject MongoClient mongoClient;
    
    @Inject
    LocationSerializer<Document> locationSerializer;

    public List<Location> list(){
        List<Location> list = new ArrayList<>();
        MongoCursor<Document> cursor = mongoClient.getDatabase("mrt").getCollection("locations").find().iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Location location = locationSerializer.deserialize(document);
                list.add(location);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public void add(Location location){
        Document document = locationSerializer.serialize(location);
        if(document != null){
            mongoClient.getDatabase("mrt").getCollection("locations").insertOne(document);
        }
    }

}