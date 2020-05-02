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

    public List<Trip> monthList(String rhid, int year, int month){
        Document query = new Document("rhid", rhid)
        .append("date.year", year)
        .append("date.month", month);
        List<Trip> list = new ArrayList<>();
        MongoCursor<Document> cursor = mongoClient.getDatabase("mrt").getCollection("trips").find(query).iterator();
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
        if(trip == null){
            return;
        }
        Document document = trip.toDocument();
        if(document != null){
            mongoClient.getDatabase("mrt").getCollection("trips").insertOne(document);
            Document query = new Document("destination", trip.getLocation().getDestination());
            Document location = mongoClient.getDatabase("mrt").getCollection("locations").find(query).first();
            if(location == null){ // it is a new location, will add to the DB
                mongoClient.getDatabase("mrt").getCollection("locations").insertOne(trip.getLocation().toDocument());
            }
        }
    }

}