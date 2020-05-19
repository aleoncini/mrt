package it.redhat.mrt.backend.model.serializer;

import it.redhat.mrt.backend.model.Location;

/**
 * This Interface represents a  Location serializer: an object that is able to serialize 
 * and deserialize Location in a specific format
 */
public interface LocationSerializer<V> {

	/**
	 * Serialize the Location object
	 * 
	 * @param location
	 * @throws IllegalArgumentException in case associate object contains invalid data
	 * @return
	 */
	public V serialize(Location location);
	
	/**
	 * Deserialize the specified object into a Location
	 * 
	 * @param serialized
	 * @throws IllegalArgumentException in case serialized object contains invalid data
	 * @return
	 */
	public Location deserialize(V serialized);
}
