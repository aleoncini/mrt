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
public class TripService {
    @MongoClientName("mrt")
    @Inject MongoClient mongoClient;

    public List<Trip> list(){
        List<Trip> list = new ArrayList<>();
        MongoCursor<Document> cursor = mongoClient.getDatabase("mrt").getCollection("trips").find().iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Trip trip = new Trip().build(document);
                list.add(trip);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public void add(Trip trip){
        Document document = trip.toDocument();
        if(document != null){
            mongoClient.getDatabase("mrt").getCollection("trips").insertOne(document);
        }
    }

}