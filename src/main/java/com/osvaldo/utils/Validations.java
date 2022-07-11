package com.osvaldo.utils;

import com.osvaldo.exception.ValidationsException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Validations {

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
