package it.redhat.mrt.backend.model;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssociateTest {

	@Test
	void testEquals() {

		Associate first = new Associate();
		Associate second = new Associate();
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
		first.setEmail("myemail");
		second.setEmail("otheremail");
		
		Assertions.assertFalse(first.equals(second));
		Assertions.assertFalse(second.equals(first));
		
		second.setEmail("myemail");
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
	}
	
	@Test
	void testToDocument() {
		
		Associate first = new Associate();
		
		Assertions.assertNull(first.toDocument());
		
		first.setName("name");
		first.setEmail("email");
		first.setCostCenter("cost");
		first.setRedhatId("id");
		first.setCarId("carid");
		first.setMileageRate(0.1);
		
		Document document = new Document("rhid", "id")
                .append("name", "name")
                .append("costCenter", "cost")
                .append("email", "email")
                .append("carId", "carid")
                .append("mileageRate", 0.1);
		
		Assertions.assertEquals(document, first.toDocument());
	}
	
	@Test
	void testBuild() {
		
		Assertions.assertNull(Associate.build(null));
		
		Associate first = new Associate();
		
		first.setName("name");
		first.setEmail("email");
		first.setCostCenter("cost");
		first.setRedhatId("id");
		first.setCarId("carid");
		first.setMileageRate(0.1);
		
		Document document = new Document("rhid", "id")
            .append("name", "name")
            .append("costCenter", "cost")
            .append("email", "email")
            .append("carId", "carid")
            .append("mileageRate", 0.1);
	
		Assertions.assertEquals(first, Associate.build(document));
	}
}
