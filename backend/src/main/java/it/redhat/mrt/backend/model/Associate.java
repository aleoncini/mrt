package it.redhat.mrt.backend.model;

import java.util.Objects;

import org.bson.Document;

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

        return Objects.equals(other.email, this.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.email);
    }

    public Document toDocument(){
        if ((rhid == null) 
            || (rhid.length() == 0) 
            || (name == null) 
            || (name.length() == 0) 
            || (costCenter == null) 
            || (costCenter.length() == 0) 
            || (carId == null)
            || (carId.length() == 0)
            || (mileageRate == 0)
        ) {
            return null;
        }
        return new Document("rhid", rhid)
                .append("name", name)
                .append("costCenter", costCenter)
                .append("email", email)
                .append("carId", carId)
                .append("mileageRate", mileageRate);
    }

    public static Associate build(Document document){

        if (document == null){
            return null;
        }

        String rhid = document.getString("rhid");
        String name = document.getString("name");
        String costCenter = document.getString("costCenter");
        String email = document.getString("email");
        String carId = document.getString("carId");
        double mileageRate = document.getDouble("mileageRate");

        return new Associate()
        			.setRedhatId(rhid)
        			.setName(name)
        			.setCostCenter(costCenter)
        			.setEmail(email)
        			.setCarId(carId)
        			.setMileageRate(mileageRate);
    }

}