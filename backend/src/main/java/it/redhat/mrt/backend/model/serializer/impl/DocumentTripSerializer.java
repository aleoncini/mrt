package it.redhat.mrt.backend.model.serializer.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;

import it.redhat.mrt.backend.model.DateOfTrip;
import it.redhat.mrt.backend.model.Location;
import it.redhat.mrt.backend.model.Trip;
import it.redhat.mrt.backend.model.serializer.TripSerializer;

/**
 * Implementation of a TripSerializer for Document objects
 */
@ApplicationScoped
public class DocumentTripSerializer implements TripSerializer<Document> {

	@Inject
	DocumentLocationSerializer documentLocationSerializer;
	
	@Inject
	DocumentDateOfTripSerializer documentdateOfTripSerializer;
	
	@Override
	public Document serialize(Trip trip) {

		 if ((trip == null)
				 || (trip.getRhid() == null)
				 || (trip.getRhid().length() == 0)
				 || (trip.getDate() == null)
				 || (trip.getLocation() == null)) {
	            throw new IllegalArgumentException("Trip object contains invalid data");
	        }

		 return new Document("rhid", trip.getRhid())
	                .append("purpose", trip.getPurpose())
	                .append("location", documentLocationSerializer.serialize(trip.getLocation()))
	                .append("date", documentdateOfTripSerializer.serialize(trip.getDate()));
	}

	@Override
	public Trip deserialize(Document document) {
		
		if (document == null
				|| (document.getString("rhid") == null)
				|| (document.getString("rhid").length() == 0)
				|| (document.getString("purpose") == null)
				|| (document.getString("purpose").length() == 0)
				|| (document.get("date") == null)
				|| !(document.get("date") instanceof Document)
				|| (document.get("location") == null)
				|| !(document.get("location") instanceof Document)
			) {
			throw new IllegalArgumentException("Document object contains invalid data");
        }

        String rhid = document.getString("rhid");
        String purpose = document.getString("purpose");
        
        Location location = documentLocationSerializer.deserialize((Document) document.get("location"));
        DateOfTrip dateOfTrip = documentdateOfTripSerializer.deserialize((Document) document.get("date"));
        
        return new Trip()
        			.setRhid(rhid)
        			.setPurpose(purpose)
        			.setLocation(location)
        			.setDate(dateOfTrip);
	}

}
