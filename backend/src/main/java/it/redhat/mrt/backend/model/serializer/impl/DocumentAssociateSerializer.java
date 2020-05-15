package it.redhat.mrt.backend.model.serializer.impl;

import javax.enterprise.context.ApplicationScoped;

import org.bson.Document;

import it.redhat.mrt.backend.model.Associate;
import it.redhat.mrt.backend.model.serializer.AssociateSerializer;

/**
 * Implementation of an AssociateSerializer for Document objects
 */
@ApplicationScoped
public class DocumentAssociateSerializer implements AssociateSerializer<Document> {

	@Override
	public Document serialize(Associate associate) {

		if (associate == null
				|| (associate.getRedhatId() == null) 
	            || (associate.getRedhatId().length() == 0) 
	            || (associate.getName() == null) 
	            || (associate.getName().length() == 0) 
	            || (associate.getCostCenter() == null) 
	            || (associate.getCostCenter().length() == 0) 
	            || (associate.getCarId() == null)
	            || (associate.getCarId().length() == 0)
	            || (associate.getMileageRate() == 0)
	        ) {
	            throw new IllegalArgumentException("Associate object contains invalid data");
	        }

	        return new Document("rhid", associate.getRedhatId())
	                .append("name", associate.getName())
	                .append("costCenter", associate.getCostCenter())
	                .append("email", associate.getEmail())
	                .append("carId", associate.getCarId())
	                .append("mileageRate", associate.getMileageRate());
	}

	@Override
	public Associate deserialize(Document document) {
		
		if (document == null
				|| (document.getString("rhid") == null)
				|| (document.getString("rhid").length() == 0)
				|| (document.getString("name") == null)
				|| (document.getString("name").length() == 0)
				|| (document.getString("costCenter") == null)
				|| (document.getString("costCenter").length() == 0)
				|| (document.getString("email") == null)
				|| (document.getString("email").length() == 0)
				|| (document.getString("carId") == null)
				|| (document.getString("carId").length() == 0)
				|| (document.getDouble("mileageRate") == 0)
			) {
			throw new IllegalArgumentException("Associate object contains invalid data");
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
