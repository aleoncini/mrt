package it.redhat.mrt.backend.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TripTest {

	@Test
	void testEquals() {

		/** test empty objects */
		Trip first = new Trip();
		Trip second = new Trip();
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
		/** test different objects */
		first = new Trip().setDate(new DateOfTrip().setDay(1).setMonth(1).setYear(2020));
		first.setLocation(new Location().setDestination("moon").setDistance(100));
		second = new Trip().setDate(new DateOfTrip().setDay(1).setMonth(1).setYear(2021));
		second.setLocation(new Location().setDestination("jupiter").setDistance(1000));
		
		Assertions.assertFalse(first.equals(second));
		Assertions.assertFalse(second.equals(first));
		
		/** test equals objects */
		second.getDate().setYear(2020);
		second.setLocation(new Location().setDestination("moon").setDistance(100));
		
		Assertions.assertTrue(first.equals(second));
		Assertions.assertTrue(second.equals(first));
		
	}
	
	@Test
	void testHashCode() {
		
		/** test empty objects */
		Trip first = new Trip();
		Trip second = new Trip();
		
		Assertions.assertEquals(first.hashCode(), second.hashCode());
		
		/** test different objects */
		first = new Trip().setDate(new DateOfTrip().setDay(1).setMonth(1).setYear(2020));
		first.setLocation(new Location().setDestination("moon").setDistance(100));
		second = new Trip().setDate(new DateOfTrip().setDay(1).setMonth(1).setYear(2021));
		second.setLocation(new Location().setDestination("jupiter").setDistance(1000));
		
		Assertions.assertNotEquals(first.hashCode(), second.hashCode());
		
		/** test equals objects */
		second.getDate().setYear(2020);
		second.setLocation(new Location().setDestination("moon").setDistance(100));
		
		Assertions.assertEquals(first.hashCode(), second.hashCode());
		
	}
	
}
