package it.redhat.mrt.pdf.model;

import java.util.Objects;

/**
 * This class represents a Date of a Trip
 */
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

        return ((other.day == this.day)
        		&& (other.month == this.month)
        		&& (other.year == this.year));
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }

}