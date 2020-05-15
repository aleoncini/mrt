package it.redhat.mrt.backend.model.serializer;

import it.redhat.mrt.backend.model.Associate;

/**
 * This Interface represents an Associate serializer: an object that is able to serialize 
 * and deserialize Associates in a specific format
 */
public interface AssociateSerializer<V> {

	/**
	 * Serialize the Associate object
	 * 
	 * @param associate
	 * @throws IllegalArgumentException in case associate object contains invalid data
	 * @return
	 */
	public V serialize(Associate associate);
	
	/**
	 * Deserialize the specified object into an Associate
	 * 
	 * @param serialized
	 * @throws IllegalArgumentException in case serialized object contains invalid data
	 * @return
	 */
	public Associate deserialize(V serialized);
}
