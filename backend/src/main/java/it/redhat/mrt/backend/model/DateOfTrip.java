package it.redhat.mrt.backend.model;

import java.util.Objects;

import org.bson.Document;

public class DateOfTrip {
    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public DateOfTrip setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public DateOfTrip setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getDay() {
        return day;
    }

    public DateOfTrip setDay(int day) {
        this.day = day;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DateOfTrip)) {
            return false;
        }

        DateOfTrip other = (DateOfTrip) obj;

        return ((other.day == this.day) && (other.month == this.month) && (other.year == this.year));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.day + this.month + this.year);
    }

    public DateOfTrip build(Document document){
        this.day = document.getInteger("day");
        this.month = document.getInteger("month");
        this.year = document.getInteger("year");
        return this;
    }

    public Document toDocument(){
        return new Document("day", this.day)
                .append("month", this.month)
                .append("year", this.year);
    }

}