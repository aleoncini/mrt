package it.redhat.mrt.backend.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AssociateTest {

	@Test
	void testEquals() {

		/** test empty objects */
		Associate first = new Associate();
		Associate second = new Associate();
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
		/** test different objects */
		first = new Associate().setEmail("myemail");
		second = new Associate().setEmail("otheremail");
		
		Assertions.assertFalse(first.equals(second));
		Assertions.assertFalse(second.equals(first));
		
		/** test equals objects */
		second.setEmail("myemail");
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
	}
	
	@Test
	void testHashCode() {
		
		/** test empty objects */
		Associate first = new Associate();
		Associate second = new Associate();
		
		Assertions.assertEquals(first.hashCode(), second.hashCode());
		
		/** test different objects */
		first = new Associate().setEmail("myemail");
		second = new Associate().setEmail("otheremail");
		
		Assertions.assertNotEquals(first.hashCode(), second.hashCode());
		
		/** test equals objects */
		second.setEmail("myemail");
		
		Assertions.assertEquals(first.hashCode(), second.hashCode());
		
	}

}
