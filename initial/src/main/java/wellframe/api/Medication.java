package wellframe.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * Represents a Medication. Medications are consumed by {@link Patient}s. A patient can take one or more medications at
 * one time. All Medications must have a unique ID or else there will be major problems with the API. Medication names
 * do not have to be unique.
 * <p>
 * Future improvements may include:
 * - Dosage
 * - Conflicting Medications
 * - Ingredients
 * - Warnings
 * - Enforced Unique IDs
 */
public class Medication implements JSONable {
    private int id;
    private String name;
    private String url;

    public static List<Medication> medicationList;

    /**
     * Creates a new Medication. This constructor should be avoided as it does not take in an ID number. Should be
     * removed when done debugging. Generates ID number using the hashcode of the String name which will cause problems
     * if we have two medicines with the same name or if we have a large enough collection of medicines that collisions
     * in hashcode begin to occur.
     *
     * @param name
     */
    public Medication(String name) {
        this.name = name;
        id = name.hashCode();
        url = "http://localhost:8080/api/v1/medications/" + id;
    }

    /**
     * Creates a new Medication object. Takes name and a unique id number.
     *
     * @param name The name of this medicine. Does not have to be unique
     * @param id   The ID number of this medicine. Must be unique.
     */
    public Medication(String name, int id) {
        this.name = name;
        this.id = id;
        url = "http://localhost:8080/api/v1/medications/" + id;
    }

    /**
     * Returns the unique ID of this medication.
     *
     * @return a unique integer ID for this medication.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the non-unique name of this Medicine.
     *
     * @return a String representing the name of this medicine.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the url of this medicine as used in the API.
     *
     * @return the url of this medicine as used in the API.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the content of this object as a JsonNode.
     *
     * @return a JsonNode containing the content of this object.
     */
    public JsonNode getContent() {
        return this.toJSON();
    }


    /**
     * Converts this object into a JSON object that can be used in RESTful APIs.
     *
     * @return a JsoNode object containing the relevant information from this object including name, id, and url.
     */
    public JsonNode toJSON() {
        ObjectMapper m = new ObjectMapper();
        ObjectNode n = m.createObjectNode();
        n.put("id", id);
        n.put("name", name);
        n.put("uri", url);

        return n;
    }

    public boolean equals(Object o) {
        if (o instanceof Medication) {
            if (((Medication) o).getId() == this.id) {
                return true;
            }
        }
        return false;

    }
}



