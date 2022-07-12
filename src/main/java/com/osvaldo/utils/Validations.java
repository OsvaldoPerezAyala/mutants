package com.osvaldo.utils;

import com.osvaldo.exception.ValidationsException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;

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
    public void validateMatrixDimension(final JSONArray dna) throws ValidationsException {
        var width = dna.length();
        if (width == 0) {
            throw new ValidationsException("dna sequences can not be empty");
        }
        for (Object sequenceObject : dna) {

            if (width != ((String) sequenceObject).length()) {
                throw new ValidationsException("Height does not belong to width");
            }
        }
    }

}
