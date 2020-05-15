package it.redhat.mrt.backend.model.serializer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import it.redhat.mrt.backend.model.Location;

@QuarkusTest
public class DocumentLocationSerializerTest {

	@Inject
    LocationSerializer<Document> locatitonSerializer;
	
	@Test
	void testSerialize() {
		
		/** Test for null */
		try {
			
			locatitonSerializer.serialize(null);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test for invalid associate */
		Location first = new Location();
		
		try {
			
			locatitonSerializer.serialize(first);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Working associate */
		first.setDestination("dest");
		first.setDistance(100);
		
		Document document = new Document("destination", "dest")
                .append("distance", 100);
		
		Assertions.assertEquals(document, locatitonSerializer.serialize(first));
	}
	
	@Test
	void testDeserialize() {
		
		/** Test for null */
		try {
			
			locatitonSerializer.deserialize(null);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test for empty */
		try {
			
			locatitonSerializer.deserialize(new Document());
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test working location */
		Location first = new Location()
				.setDestination("dest")
				.setDistance(100);
		
		Document document = new Document("destination", "dest")
            .append("distance", 100);
	
		Assertions.assertEquals(first, locatitonSerializer.deserialize(document));
		
		/** Test working location */
		Document document2 = new Document("destination", "dest")
            .append("distance", new Integer(100));
	
		Assertions.assertEquals(first, locatitonSerializer.deserialize(document2));
	}
	
}
