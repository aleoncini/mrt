package it.redhat.mrt.backend.model.serializer;

import it.redhat.mrt.backend.model.Trip;

/**
 * This interface represents a Trip serializer: an object that is able to serialize
 * and deserialize Trip in a specific format
 */
public interface TripSerializer<V> {

	/**
	 * Serialize the Trip object
	 * 
	 * @param trip
	 * @throws IllegalArgumentException in case trip object contains invalid data
	 * @return
	 */
	public V serialize(Trip trip);
	
	/**
	 * Deserialize the specified object into a Trip
	 * 
	 * @param serialized
	 * @throws IllegalArgumentException in case serialized object contains invalid data
	 * @return
	 */
	public Trip deserialize(V serialized);
}
