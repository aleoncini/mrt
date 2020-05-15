package it.redhat.mrt.backend.model.serializer;

import it.redhat.mrt.backend.model.DateOfTrip;

/**
 * This interface represents a DateOfTrip serializer: an object that is able to serialize
 * and deserialize DateOfTrip in a specific format
 */
public interface DateOfTripSerializer<V> {

	/**
	 * Serialize the DateOfTrip object
	 * 
	 * @throws IllegalArgumentException in case trip object contains invalid data
	 * @return
	 */
	public V serialize(DateOfTrip dateOfTrip);
	
	/**
	 * Deserialize the specified object into a DateOfTrip
	 * 
	 * @param serialized
	 * @throws IllegalArgumentException in case serialized object contains invalid data
	 * @return
	 */
	public DateOfTrip deserialize(V serialized);
}
