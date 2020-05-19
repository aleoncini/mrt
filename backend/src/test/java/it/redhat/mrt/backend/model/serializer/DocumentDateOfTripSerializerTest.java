package it.redhat.mrt.backend.model.serializer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import it.redhat.mrt.backend.model.DateOfTrip;

@QuarkusTest
public class DocumentDateOfTripSerializerTest {

	@Inject
    DateOfTripSerializer<Document> dateOfTripSerializer;
	
	@Test
	void testSerialize() {
		
		/** Test for null */
		try {
			
			dateOfTripSerializer.serialize(null);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test for invalid associate */
		DateOfTrip first = new DateOfTrip();
		
		try {
			
			dateOfTripSerializer.serialize(first);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Working associate */
		first.setDay(1).setMonth(1).setYear(2020);
		
		Document document = new Document("day", 1)
                .append("month", 1)
                .append("year", 2020);
		
		Assertions.assertEquals(document, dateOfTripSerializer.serialize(first));
	}
	
	@Test
	void testDeserialize() {
		
		/** Test for null */
		try {
			
			dateOfTripSerializer.deserialize(null);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test for empty */
		try {
			
			dateOfTripSerializer.deserialize(new Document());
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test working location */
		DateOfTrip first = new DateOfTrip()
				.setDay(1)
				.setMonth(2)
				.setYear(2020);
		
		Document document = new Document("day", 1)
                .append("month", 2)
                .append("year", 2020);
	
		Assertions.assertEquals(first, dateOfTripSerializer.deserialize(document));
		
		/** Test working location */
		Document document2 = new Document("day", new Integer(1))
                .append("month", new Integer(2))
                .append("year", new Integer(2020));
	
		Assertions.assertEquals(first, dateOfTripSerializer.deserialize(document2));
	}
	
}
