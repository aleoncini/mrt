package it.redhat.mrt.backend.model.serializer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import it.redhat.mrt.backend.model.DateOfTrip;
import it.redhat.mrt.backend.model.Location;
import it.redhat.mrt.backend.model.Trip;

@QuarkusTest
public class DocumentTripSerializerTest {

	@Inject
    TripSerializer<Document> tripSerializer;
	
	@Test
	void testSerialize() {
		
		/** Test for null */
		try {
			
			tripSerializer.serialize(null);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test for invalid associate */
		Trip first = new Trip();
		
		try {
			
			tripSerializer.serialize(first);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Working associate */
		first.setRhid("123");
		first.setPurpose("test");
		first.setLocation(new Location().setDestination("moon").setDistance(100));
		first.setDate(new DateOfTrip().setDay(1).setMonth(1).setYear(2020));
		
		Document location = new Document("destination", "moon")
                .append("distance", 100);
		
		Document date = new Document("day", 1)
                .append("month", 1)
                .append("year", 2020);
		
		Document document = new Document("rhid", "123")
                .append("purpose", "test")
                .append("location", location)
                .append("date", date);
		
		Assertions.assertEquals(document, tripSerializer.serialize(first));
	}
	
	@Test
	void testDeserialize() {
		
		/** Test for null */
		try {
			
			tripSerializer.deserialize(null);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test for empty */
		try {
			
			tripSerializer.deserialize(new Document());
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test working location */
		Trip first = new Trip()
				.setRhid("123")
				.setPurpose("test")
				.setLocation(new Location().setDestination("moon").setDistance(100))
				.setDate(new DateOfTrip().setDay(1).setMonth(1).setYear(2020));
		
		Document location = new Document("destination", "moon")
                .append("distance", 100);
		
		Document date = new Document("day", 1)
                .append("month", 1)
                .append("year", 2020);
		
		Document document = new Document("rhid", "123")
                .append("purpose", "test")
                .append("location", location)
                .append("date", date);
	
		Assertions.assertEquals(first, tripSerializer.deserialize(document));
	}
	
}
