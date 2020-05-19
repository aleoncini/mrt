package it.redhat.mrt.backend.model.serializer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import it.redhat.mrt.backend.model.Associate;

@QuarkusTest
public class DocumentAssociateSerializerTest {

	@Inject
    AssociateSerializer<Document> associateSerializer;
	
	@Test
	void testSerialize() {
		
		/** Test for null */
		try {
			
			associateSerializer.serialize(null);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test for invalid associate */
		Associate first = new Associate();
		
		try {
			
			associateSerializer.serialize(first);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Working associate */
		first.setName("name");
		first.setEmail("email");
		first.setCostCenter("cost");
		first.setRhid("id");
		first.setCarId("carid");
		first.setMileageRate(0.1);
		
		Document document = new Document("rhid", "id")
                .append("name", "name")
                .append("costCenter", "cost")
                .append("email", "email")
                .append("carId", "carid")
                .append("mileageRate", 0.1);
		
		Assertions.assertEquals(document, associateSerializer.serialize(first));
	}
	
	@Test
	void testDeserialize() {
		
		/** Test for null */
		try {
			
			associateSerializer.deserialize(null);
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test for empty */
		try {
			
			associateSerializer.deserialize(new Document());
			fail("Expected IllegalArgumentException");
		
		} catch (IllegalArgumentException ex) {
		
			assertTrue(true);
		
		}
		
		/** Test working associate */
		Associate first = new Associate()
				.setName("name")
				.setEmail("email")
				.setCostCenter("cost")
				.setRhid("id")
				.setCarId("carid")
				.setMileageRate(0.1);
		
		Document document = new Document("rhid", "id")
            .append("name", "name")
            .append("costCenter", "cost")
            .append("email", "email")
            .append("carId", "carid")
            .append("mileageRate", 0.1);
	
		Assertions.assertEquals(first, associateSerializer.deserialize(document));
	}
	
}
