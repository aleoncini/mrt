package it.redhat.mrt.backend.model;

import java.util.Objects;

/**
 * This class represents an Associate
 */
public class Associate {

    private String name;
    private String email;
    private String costCenter;
    private String rhid;
    private String carId;
    private double mileageRate;

    public String getName() {
        return name;
    }

    public Associate setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Associate setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public Associate setCostCenter(String costCenter) {
        this.costCenter = costCenter;
        return this;
    }

    public String getRedhatId() {
        return rhid;
    }

    public Associate setRedhatId(String id) {
        this.rhid = id;
        return this;
    }

    public String getCarId() {
        return this.carId;
    }

    public Associate setCarId(String carId) {
        this.carId = carId;
        return this;
    }

    public double getMileageRate() {
        return this.mileageRate;
    }

    public Associate setMileageRate(double rate) {
        this.mileageRate = rate;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Associate)) {
            return false;
        }

        Associate other = (Associate) obj;

        return Objects.equals(other.name, this.name)
        		&& Objects.equals(other.email, this.email)
        		&& Objects.equals(other.costCenter, this.costCenter)
        		&& Objects.equals(other.rhid, this.rhid)
        		&& Objects.equals(other.carId, this.carId)
        		&& (other.mileageRate == this.mileageRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, costCenter, rhid, carId, mileageRate);
    }

}