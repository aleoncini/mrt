package it.redhat.mrt.pdf.model;

import java.util.Objects;

/**
 * This class represents a Location
 */
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

        return Objects.equals(other.destination, this.destination)
        		&& (other.distance == this.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, distance);
    }

}