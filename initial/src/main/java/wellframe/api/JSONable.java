package wellframe.api;


import com.fasterxml.jackson.databind.JsonNode;

/**
 * This interface provides the functionality that allows an object to be converted into a JSON object.
 */
public interface JSONable {
    /**
     * Converts this object into a JSON-style notation object using the jackson json library for Java.
     * @return a JsonNode object representing the information contained within this class.
     */
    JsonNode toJSON();
}
