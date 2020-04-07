package it.redhat.mrt.backend.model;

import java.util.Objects;

import org.bson.Document;

public class Trip {
    private String associateId;
    private DateOfTrip date;
    private Location location;
    private String purpose;

    public String getAssociateUid() {
        return associateId;
    }

    public Trip setAssociateId(String id) {
        this.associateId = id;
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

        return ((this.date.equals(other.date)) && ((this.location.equals(other.location))));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.location.hashCode() + this.date.hashCode());
    }

    public Trip build(Document document){
        if (document == null){
            return null;
        }
        Document dateDoc = (Document) document.get("date");
        Document locationDoc = (Document) document.get("location");
        this.associateId = document.getString("associateId");
        this.purpose = document.getString("purpose");
        this.location = new Location().build(locationDoc);
        this.date = new DateOfTrip().build(dateDoc);
        return this;
    }

    public Document toDocument(){
        if ((associateId == null) || (associateId.length() == 0) || (date == null) || (location == null)){
            return null;
        }
        return new Document("associateId", associateId)
                .append("purpose", purpose)
                .append("location", location.toDocument())
                .append("date", date.toDocument());
    }

}