package it.redhat.mrt.backend.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class LocationTest {

	@Test
	void testEquals() {

		/** test empty objects */
		Location first = new Location();
		Location second = new Location();
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
		/** test different objects */
		first = new Location().setDestination("moon");
		second = new Location().setDestination("jupiter");
		
		Assertions.assertFalse(first.equals(second));
		Assertions.assertFalse(second.equals(first));
		
		/** test equals objects */
		second.setDestination("moon");
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
	}
	
	@Test
	void testHashCode() {
		
		/** test empty objects */
		Location first = new Location();
		Location second = new Location();
		
		Assertions.assertEquals(first.hashCode(), second.hashCode());
		
		/** test different objects */
		first = new Location().setDestination("moon");
		second = new Location().setDestination("jupiter");
		
		Assertions.assertNotEquals(first.hashCode(), second.hashCode());
		
		/** test equals objects */
		second.setDestination("moon");
		
		Assertions.assertEquals(first.hashCode(), second.hashCode());
		
	}
	
}
