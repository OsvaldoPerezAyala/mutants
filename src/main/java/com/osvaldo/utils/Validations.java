package com.osvaldo.utils;

import com.osvaldo.exception.ValidationsException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class Validations {

    /**
     * This method validate the payload structure
     *
     * @param payload the payload received
     * @return JSONObject who contains the JSON structure
     */
    public JSONObject validatePayload(final String payload) throws ValidationsException {

        var dnaJSONObject = new JSONObject(payload);
        if (!dnaJSONObject.has("dna")) {
            throw new ValidationsException("dna array does not exist");
        }

        if (!(dnaJSONObject.get("dna") instanceof JSONArray)) {
            throw new ValidationsException("dna array is not a valid type");
        }
        return dnaJSONObject;
    }

    /**
     * This method validate the DNA matrix NxN dimensions
     *
     * @param dna contains the DNA sequences array
     */
    public void validateMatrixDimensionAndProteinsAllowd(final JSONArray dna) throws ValidationsException {
        var width = dna.length();
        if (width == 0) {
            throw new ValidationsException("dna sequences can not be empty");
        }
        for (Object sequenceObject : dna) {

            var sequence = ((String) sequenceObject);
            if (width != sequence.length()) {
                throw new ValidationsException("Height does not belong to width");
            }
            this.validateAllowedProteins(sequence);
        }
    }

    /**
     * This method validate if the proteins received from each sequence ara valid, TGCA ar only valids
     *
     * @param sequence Contains the provided sequence
     * @throws ValidationsException in case the sequence contains not valid protein throw validation error
     */
    private void validateAllowedProteins(final String sequence) throws ValidationsException {
        var regex = "[TGAC]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sequence.toUpperCase());
        if (!matcher.matches()) {
            throw new ValidationsException("The proteins in the sequence is not valid");
        }
    }

}
