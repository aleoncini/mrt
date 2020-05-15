package it.redhat.mrt.backend.model.serializer.impl;

import javax.enterprise.context.ApplicationScoped;

import org.bson.Document;

import it.redhat.mrt.backend.model.DateOfTrip;
import it.redhat.mrt.backend.model.serializer.DateOfTripSerializer;

/**
 * Implementation of a DateOfTripSerializer for Document objects
 */
@ApplicationScoped
public class DocumentDateOfTripSerializer implements DateOfTripSerializer<Document> {

	@Override
	public Document serialize(DateOfTrip dateOfTRip) {

		if (dateOfTRip == null
				|| (dateOfTRip.getDay() == 0)
				|| (dateOfTRip.getMonth() == 0)
				|| (dateOfTRip.getYear() == 0)) {
		
			throw new IllegalArgumentException("DateOfTrip object contains invalid data");
	        
		}
		
		return new Document("day", dateOfTRip.getDay())
				.append("month", dateOfTRip.getMonth())
				.append("year", dateOfTRip.getYear());
	}

	@Override
	public DateOfTrip deserialize(Document document) {
		
		if (document == null
				|| (document.getInteger("day") == null)
				|| (document.getInteger("month") == null)
				|| (document.getInteger("year") == null)
			) {
			throw new IllegalArgumentException("Document object contains invalid data");
        }

        return new DateOfTrip()
        			.setDay(document.getInteger("day"))
        			.setMonth(document.getInteger("month"))
        			.setYear(document.getInteger("year"));
	}

}
