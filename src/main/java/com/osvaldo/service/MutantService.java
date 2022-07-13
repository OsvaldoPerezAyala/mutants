package com.osvaldo.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.osvaldo.exception.ValidationsException;
import com.osvaldo.repository.Repository;
import com.osvaldo.utils.Validations;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;

import static com.osvaldo.utils.Constants.*;

@ApplicationScoped
public class MutantService {

    @ConfigProperty(name = "connection_chain")
    String chainConnection;
    @Inject
    Validations validations;
    @Inject
    Repository repository;

    /**
     * This method receive the original payload
     *
     * @param payload contains data to evaluate if provided DNA is mutant or human
     * @return if the provided DNA uas mutant or not
     * @throws ValidationsException controlled exceptions about if the DNA is not computable
     */
    public boolean isMutant(final String payload) throws ValidationsException {

        var dnaJSONObject = validations.validatePayload(payload);
        var dnaJSONArray = dnaJSONObject.getJSONArray(DNA);
        validations.validateMatrixDimensionAndProteinsAllowd(dnaJSONArray);
        var dnaMatrix = this.buildDNAMatrix(dnaJSONObject.getJSONArray(DNA));
        var sequences = this.determinateSequences(dnaMatrix);
        var isMutant = sequences > 1;
        this.saveDNASequences(dnaJSONObject, isMutant);
        return isMutant;
    }

    /**
     * This method build a dna char matrix
     *
     * @param dnaJSONArray this array contains each DNA sequence
     * @return a char matrix who contains each sequence in the exact order to be received
     */
    private char[][] buildDNAMatrix(final JSONArray dnaJSONArray) {

        var dna = dnaJSONArray.toList().toArray(String[]::new);
        var array = Arrays.stream(dna).map(String::toCharArray);
        return array.toArray(char[][]::new);
    }

    /**
     * This method determinate how many mutant sequences contains the provided DNA
     *
     * @param dnaMatrix Contains all DNA sequences provided
     * @return counter of mutant sequences found
     */
    public int determinateSequences(char[][] dnaMatrix) {

        int totalSequences = 0;
        for (int row = 0; row < dnaMatrix.length; row++) {
            for (int col = 0; col < dnaMatrix[row].length; col++) {

                totalSequences += this.hasObliqueRightSequences(dnaMatrix, row, col);
                totalSequences += this.hasObliqueLeftSequences(dnaMatrix, row, col);
                totalSequences += this.hasHorizontalSequences(dnaMatrix, row, col);
                totalSequences += this.hasVerticalSequence(dnaMatrix, row, col);
            }
            if (totalSequences > 1) {
                break;
            }
        }
        return totalSequences;
    }

    /**
     * This method determinate how many sequences in oblique right way exist in the DNA matrix
     *
     * @param charDNAMatrix contains DNA sequences provided
     * @param row           current row position
     * @param col           current column position
     * @return counter of mutant sequences found
     */
    private int hasObliqueRightSequences(final char[][] charDNAMatrix, final int row, final int col) {

        int tempRow = row;
        int tempCol = col;
        int obliqueRightCounter = 0;
        int totalSequences = 0;
        char temp = 0;

        for (int aux = 0; aux < charDNAMatrix.length; aux++) {
            if (charDNAMatrix.length > tempRow && charDNAMatrix.length > tempCol && (row == 0 || col == 0)) {
                char letter = charDNAMatrix[tempRow++][tempCol++];
                if (aux == 0) {
                    temp = letter;
                } else {
                    if (letter == temp) {
                        obliqueRightCounter++;
                        if (obliqueRightCounter == 3) {
                            totalSequences++;
                        }
                    } else {
                        temp = letter;
                        obliqueRightCounter = 0;
                    }
                }
            }
        }

        return totalSequences;
    }

    /**
     * This method determinate how many sequences in oblique left way exist in the DNA matrix
     *
     * @param charDNAMatrix contains DNA sequences provided
     * @param row           current row position
     * @param col           current column position
     * @return counter of mutant sequences found
     */
    private int hasObliqueLeftSequences(final char[][] charDNAMatrix, final int row, final int col) {

        int tempRow = row;
        int tempCol = col;
        int obliqueLeftCounter = 0;
        int totalSequences = 0;
        char temp = 0;

        for (int aux = 0; aux < charDNAMatrix.length; aux++) {

            if (charDNAMatrix.length > tempRow && tempCol >= 0 && (row == 0 || col == charDNAMatrix.length - 1)) {
                char letter = charDNAMatrix[tempRow++][tempCol--];
                if (aux == 0) {
                    temp = letter;
                } else {
                    if (letter == temp) {
                        obliqueLeftCounter++;
                        if (obliqueLeftCounter == 3) {
                            totalSequences++;
                        }
                    } else {
                        temp = letter;
                        obliqueLeftCounter = 0;
                    }
                }
            }
        }

        return totalSequences;
    }

    /**
     * This method determinate how many sequences in horizontal way exist in the DNA matrix
     *
     * @param charDNAMatrix contains DNA sequences provided
     * @param row           current row position
     * @param col           current column position
     * @return counter of mutant sequences found
     */
    private int hasHorizontalSequences(final char[][] charDNAMatrix, final int row, final int col) {
        int tempCol = col;
        int totalSequences = 0;
        int rowCounter = 0;
        char temp = 0;

        if (col == 0) {
            for (int aux = 0; aux < charDNAMatrix.length; aux++) {
                char letter = charDNAMatrix[row][tempCol++];
                if (aux == 0) {
                    temp = letter;
                } else {
                    if (letter == temp) {
                        rowCounter++;
                        if (rowCounter == 3) {
                            totalSequences++;
                        }
                    } else {
                        temp = letter;
                        rowCounter = 0;
                    }
                }
            }
        }
        return totalSequences;
    }

    /**
     * This method determinate how many sequences in Vertical way exist in the DNA matrix
     *
     * @param charDNAMatrix contains DNA sequences provided
     * @param row           current row position
     * @param col           current column position
     * @return counter of mutant sequences found
     */
    private int hasVerticalSequence(final char[][] charDNAMatrix, final int row, final int col) {
        int temp4Fila = row;
        int totalSequences = 0;
        int colCounter = 0;
        char temp = 0;

        if (row == 0) {
            for (int aux = 0; aux < charDNAMatrix.length; aux++) {
                char letter = charDNAMatrix[temp4Fila++][col];
                if (aux == 0) {
                    temp = letter;
                } else {
                    if (letter == temp) {
                        colCounter++;
                        if (colCounter == 3) {
                            totalSequences++;
                        }
                    } else {
                        temp = letter;
                        colCounter = 0;
                    }
                }
            }
        }
        return totalSequences;
    }

    /**
     * This method save the original request and if the DNA is mutant
     *
     * @param dna      contains DNA sequences provided in JSONObject format
     * @param isMutant contains if DNA is mutant or not
     */
    private void saveDNASequences(final JSONObject dna, final boolean isMutant) {

        var document = Document.parse(dna.toString());
        document.put(IS_MUTANT, isMutant);
        MongoDatabase database = repository.getMongoClient().getDatabase(CAFE_DB);
        MongoCollection<Document> dnaCollection = database.getCollection(DNA);

        repository.insertDNA(dnaCollection, document);
    }

}


