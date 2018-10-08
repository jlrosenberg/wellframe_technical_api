package wellframe.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is the controller class for the Wellframe RESTful API I am building.
 */
@SuppressWarnings("MagicConstant")
@RestController
public class Controller {

    private final AtomicLong counter = new AtomicLong();
    Map<Integer, Medication> medicineMap = new HashMap<Integer, Medication>();
    static int currentMedicineID = 0;
    static int currentPatientID = 0;
    Map<Integer, Patient> patientMap = new HashMap<Integer, Patient>();


    /**
     * This method handles the GET call made to medications with a medicine ID.
     *
     * @param id The Unique integer ID of the medication to access.
     * @return A JSON Object containing the medication details or a 400 response.
     */
    @RequestMapping(value = "api/v1/medications", method = RequestMethod.GET)
    public ResponseEntity<Medication> medicationGet(@RequestParam(value = "id", defaultValue = "-1") int id) {

        if (!medicineMap.containsKey(id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(medicineMap.get(id).toJSON(), HttpStatus.OK);
    }

    /**
     * Adds a medication to the list of available medications.
     * @param name Name of the medication to add.
     * @return The medication object that has been created.
     */
    @RequestMapping(value = "api/v1/medications", method = RequestMethod.POST)
    public ResponseEntity<Medication> medicationPost(@RequestParam(value = "name", defaultValue = "EMPTYNAME") String name) {
        if (name.equals("EMPTYNAME")) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        medicineMap.put(currentMedicineID, new Medication(name, currentMedicineID));
        currentMedicineID++;
        return new ResponseEntity(medicineMap.get(currentMedicineID - 1).toJSON(), HttpStatus.OK);
    }

    /**
     * Removes a medication from the list of available medications.
     * @param id The id of the medication to remove.
     * @return The success or error http code.
     */
    @RequestMapping(value = "api/v1/medications", method = RequestMethod.DELETE)
    public ResponseEntity<Medication> medication(@RequestParam(value = "id", defaultValue = "-1") int id) {

        //Check to see if the medicine even exists, give bad request if it does not
        if (!medicineMap.containsKey(id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //Remove the medicine from the medicine map.
        medicineMap.remove(id);
        //Give good request if it is successful.
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Add a patient to the map of current patients.
     * @param firstName The first name of the patient to be added
     * @param lastName The last name of the patient to be added
     * @param age The age of the patient to be added
     * @return The patient object that has been created
     */
    @RequestMapping(value = "api/v1/patients", method = RequestMethod.POST)
    public ResponseEntity<Patient> patientPost(@RequestParam(value = "firstname", defaultValue = "empty") String firstName,
                                               @RequestParam(value = "lastname", defaultValue = "empty") String lastName,
                                               @RequestParam(value = "age", defaultValue = "-1") int age
                                               ) {
        //System.out.println("HERE");

        if (firstName.equals("empty") || lastName.equals("empty") || age<0)  {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        patientMap.put(currentPatientID, new Patient(firstName, lastName, age, currentPatientID));
        currentPatientID++;
        return new ResponseEntity(patientMap.get(currentPatientID - 1).toJSON(), HttpStatus.OK);
    }

    /**
     * This method handles the GET call made to medications with a medicine ID.
     *
     * @param id The Unique integer ID of the medication to access.
     * @return A JSON Object containing the medication details or a 400 response.
     */
    @RequestMapping(value = "api/v1/patients", method = RequestMethod.GET)
    public ResponseEntity<Patient> patientGet(@RequestParam(value = "id", defaultValue = "-1") int id) {

        if (!patientMap.containsKey(id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(patientMap.get(id).toJSON(), HttpStatus.OK);
    }


    /**
     * Add a medication to the list of medications that a patient is taking
     * @param medId The ID of the medication to add to a patients medication list
     * @param id The ID of the patient
     * @return The updated medication list of the patient
     */
    @RequestMapping(value = "api/v1/patients/{id}/medications", method = RequestMethod.POST)
    public ResponseEntity<Medication> patientMedicationPost(@RequestParam(value = "id", defaultValue = "-1") int medId,
                                                            @PathVariable int id) {

        //If the patient or medicine does not exist or if the patient is already on this medicine.
        if (!medicineMap.containsKey(medId) || !patientMap.containsKey(id) ||
                patientMap.get(id).getMedications().contains(medicineMap.get(medId))) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        patientMap.get(id).getMedications().add(medicineMap.get(medId));
        return new ResponseEntity(patientMap.get(id).getMedicationListAsJson(), HttpStatus.OK);
    }

    /**
     * Removes a medication from a patients medication list.
     * @param medId The ID of the medication to remove
     * @param patientId The ID of the patient that the medicine is being removed from
     * @return The http success or failure code.
     */
    @RequestMapping(value = "api/v1/patients/{patientId}/medications/{medId}", method = RequestMethod.DELETE)
    public ResponseEntity<Medication> patientMedicationDelete(@PathVariable int medId,
                                                            @PathVariable int patientId) {

        //If the patient or medicine does not exist or if the patient is not already on this medicine.
        if (!medicineMap.containsKey(medId) || !patientMap.containsKey(patientId) ||
                !patientMap.get(patientId).getMedications().contains(medicineMap.get(medId))) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        patientMap.get(patientId).getMedications().remove(medicineMap.get(medId));
        return new ResponseEntity(HttpStatus.OK);
    }

}