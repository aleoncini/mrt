package it.redhat.mrt.model;

public class DateOfTrip {
    
    public int year;
    public int month;
    public int day;

    public DateOfTrip setYear(int year) {
        this.year = year;
        return this;
    }

    public DateOfTrip setMonth(int month) {
        this.month = month;
        return this;
    }

    public DateOfTrip setDay(int day) {
        this.day = day;
        return this;
    }

}