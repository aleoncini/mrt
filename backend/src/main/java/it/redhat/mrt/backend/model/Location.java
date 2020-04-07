package it.redhat.mrt.backend.model;

import java.util.Objects;

import org.bson.Document;

public class Location {
    private String destination;
    private int distance;

    public String getDestination() {
        return destination;
    }

    public Location setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public Location setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Location)) {
            return false;
        }

        Location other = (Location) obj;

        return Objects.equals(other.destination, this.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.destination);
    }

    public Location build(Document document){
        if (document == null){
            return null;
        }
        this.destination = document.getString("destination");
        this.distance = document.getInteger("distance");
        return this;
    }

    public Document toDocument(){
        if ((destination == null) || (destination.length() == 0)){
            return null;
        }
        return new Document("destination", destination).append("distance", distance);
    }

}