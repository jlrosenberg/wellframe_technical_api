package wellframe.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Patient in the Wellframe API. A patient has a first name, last name, age, and a list of medications
 * that they are currently taking. In this iteration of the patient class, only the medications list is being mutated
 * after construction.
 *
 * Potential future improvements:
 *          - Add patient allergies
 *          - Add medicine dosages
 *          - Add medical history
 *          - Add previous medicines
 */
public class Patient implements JSONable{
    private String firstName;
    private String lastName;
    private String url;
    private int age;
    private int id;
    List<Medication> medications;

    /**
     * Creates a new Patient object.
     * @param firstName The first name of the patient
     * @param lastName The last name of the patient
     * @param age The age of the patient
     * @param id The unique ID number of the patient
     */
    public Patient(String firstName, String lastName, int age, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.id = id;
        this.url = "http://localhost:8080/api/v1/patients/" + id;
        medications=new ArrayList<Medication>();
    }

    /**
     * Gets the first name of the patient.
     * @return a String containing the first name of the patient
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the patient.
     * @return a String containing the last name of the patient
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the age of the patient
     * @return a String containing the age of the patient
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the list of medications used by a patient
     * @return the list of medications used by the patient
     */
    public List<Medication> getMedications(){
        return medications;
    }

    /**
     * Gets the list of medications used by a patient and formats it in JSON for use in a RESTful API.
     * @return a JSON-style list of medications used by the patient.
     */
    public JsonNode getMedicationListAsJson(){

        ObjectMapper m = new ObjectMapper();
        ArrayNode meds=m.createArrayNode();
        for(Medication med:medications){
            meds.add(med.toJSON());
        }

        return meds;
    }

    @Override
    public JsonNode toJSON() {
        ObjectMapper m = new ObjectMapper();
        ObjectNode n = m.createObjectNode();
        n.put("id", id);
        n.put("firstName", firstName);
        n.put("lastName", lastName);
        n.put("age", age);

        //Put all of the patients medications into an ArrayNode.
        ArrayNode meds=m.createArrayNode();
        for(Medication med:medications){
            meds.add(med.toJSON());
        }

        n.putPOJO("medications", meds);
        n.put("uri", url);


        return n;
    }
}
