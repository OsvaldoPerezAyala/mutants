package com.osvaldo.service;

import com.osvaldo.exception.CustomizedException;
import com.osvaldo.repository.Repository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;

@ApplicationScoped
public class Service {

    @ConfigProperty(name = "connection_chain")
    String chainConnection;

    @Inject
    Repository repository;

    public boolean isMutant(JSONObject payloadObject) throws CustomizedException {

        var dnaMatrix = this.buildMatrix(payloadObject.getJSONArray("dna").toList().toArray(String[]::new));
        this.validateMatrix(dnaMatrix);
        var sequences = this.determinateSequences(dnaMatrix);
        var isMutant = sequences > 1;
        this.saveResult(payloadObject, isMutant);
        return isMutant;
    }

    public void saveResult(final JSONObject dna, final boolean isMutant) {

        dna.put("is_mutant", isMutant);
        //Repository repository = new Repository(chainConnection);
        repository.insertDNA(repository.getMongoClient(), dna);
    }

    private char[][] buildMatrix(String[] dna) {
        var array = Arrays.stream(dna).map(String::toCharArray);
        return array.toArray(char[][]::new);
    }

    private void validateMatrix(char[][] charMatrix) throws CustomizedException {
        var large = charMatrix.length;
        var tall = charMatrix[0].length;

        if (large != tall) {
            throw new CustomizedException("Height does not belong to width");
        }
    }

    public int determinateSequences(char[][] charMatrix) {

        int totalSequences = 0;
        for (int fila = 0; fila < charMatrix.length; fila++) {
            for (int col = 0; col < charMatrix[fila].length; col++) {
                int colCounter = 0;
                int rowCounter = 0;
                int obliqueRightCounter = 0;
                int obliqueLeftCounter = 0;

                int tempFila = fila;
                int tempCol = col;
                char temp = 0;
                for (int aux = 0; aux < charMatrix.length; aux++) {

                    if (charMatrix.length > tempFila && charMatrix.length > tempCol && (fila == 0 || col == 0)) {

                        char letter = charMatrix[tempFila++][tempCol++];
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

                int temp2Fila = fila;
                int temp2Col = col;

                for (int aux = 0; aux < charMatrix.length; aux++) {

                    if (charMatrix.length > temp2Fila && temp2Col >= 0 && (fila == 0 || col == charMatrix.length - 1)) {
                        char letter = charMatrix[temp2Fila++][temp2Col--];
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

                int temp3Col = col;

                if (col == 0) {
                    for (int aux = 0; aux < charMatrix.length; aux++) {
                        char letter = charMatrix[fila][temp3Col++];
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

                int temp4Fila = fila;

                if (fila == 0) {
                    for (int aux = 0; aux < charMatrix.length; aux++) {
                        char letter = charMatrix[temp4Fila++][col];
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
            }
            if (totalSequences > 1) {
                break;
            }
        }
        return totalSequences;
    }

}


