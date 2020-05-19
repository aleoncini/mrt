package it.redhat.mrt.backend.model;

import java.util.Objects;

/**
 * This class represents a Trip
 */
public class Trip {

    private String rhid;
    private DateOfTrip date;
    private Location location;
    private String purpose;

    public String getRhid() {
        return rhid;
    }

    public Trip setRhid(String id) {
        this.rhid = id;
        return this;
    }

    public DateOfTrip getDate() {
        return date;
    }

    public Trip setDate(DateOfTrip date) {
        this.date = date;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public Trip setLocation(Location location) {
        this.location = location;
        return this;
    }

    public String getPurpose() {
        return purpose;
    }

    public Trip setPurpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Trip)) {
            return false;
        }

        Trip other = (Trip) obj;

        return Objects.equals(other.rhid, this.rhid)
        		&& (Objects.equals(other.date, this.date))
        		&& (Objects.equals(other.location, this.location))
        		&& (Objects.equals(other.purpose, this.purpose));
        
    }

    @Override
    public int hashCode() {
        return Objects.hash(rhid, date, location, purpose);
    }

}