package it.redhat.mrt.backend.model.serializer.impl;

import javax.enterprise.context.ApplicationScoped;

import org.bson.Document;

import it.redhat.mrt.backend.model.Location;
import it.redhat.mrt.backend.model.serializer.LocationSerializer;

/**
 * Implementation of a LocationSerializer for Document objects
 */
@ApplicationScoped
public class DocumentLocationSerializer implements LocationSerializer<Document> {

	@Override
	public Document serialize(Location location) {

		if (location == null
				|| (location.getDestination() == null)
				|| (location.getDestination().length() == 0)) {
		
			throw new IllegalArgumentException("Location object contains invalid data");
	        
		}
		
		return new Document("destination", location.getDestination())
				.append("distance", location.getDistance());
	}

	@Override
	public Location deserialize(Document document) {
		
		if (document == null
				|| (document.getString("destination") == null)
				|| (document.getString("destination").length() == 0)
				|| (document.get("distance") == null)
				|| !(document.get("distance") instanceof Integer)
			) {
			throw new IllegalArgumentException("Location object contains invalid data");
        }

        String destination = document.getString("destination");
        Integer distance = document.getInteger("distance");

        return new Location()
        			.setDestination(destination)
        			.setDistance(distance);
	}

}
