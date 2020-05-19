package it.redhat.mrt.backend.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class DateOfTripTest {

	@Test
	void testEquals() {

		/** test empty objects */
		DateOfTrip first = new DateOfTrip();
		DateOfTrip second = new DateOfTrip();
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
		/** test different objects */
		first = new DateOfTrip().setDay(1).setMonth(1).setYear(2020);
		second = new DateOfTrip().setDay(1).setMonth(1).setYear(2021);
		
		Assertions.assertFalse(first.equals(second));
		Assertions.assertFalse(second.equals(first));
		
		/** test equals objects */
		second.setYear(2020);
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
	}
	
	@Test
	void testHashCode() {
		
		/** test empty objects */
		DateOfTrip first = new DateOfTrip();
		DateOfTrip second = new DateOfTrip();
		
		Assertions.assertEquals(first.hashCode(), second.hashCode());
		
		/** test different objects */
		first = new DateOfTrip().setDay(1).setMonth(1).setYear(2020);
		second = new DateOfTrip().setDay(1).setMonth(1).setYear(2021);
		
		Assertions.assertNotEquals(first.hashCode(), second.hashCode());
		
		/** test equals objects */
		second.setYear(2020);
		
		Assertions.assertEquals(first.hashCode(), second.hashCode());
		
	}
	
}
