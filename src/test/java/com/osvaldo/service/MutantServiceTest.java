package com.osvaldo.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class MutantServiceTest {

    @Inject
    MutantService mutantService;

    @Test
    public void testArray() throws Exception {
      /* String[] dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        String[] dna2 = new String[]{"AAAATC", "GTAACA", "TTTGCG", "GGGGCC", "TGACCC", "GGAACT"};
        String[] dna3 = new String[]{"AACATC", "GTAACA", "TTTGCG", "GTGGAC", "TGCCCC", "GGAACT"};
        String[] dna4 = new String[]{"AACATCG", "GTAACAG", "TTTGCGG", "GTGGACG", "TGCCCCT", "GGACCTT", "ATCTTTT"};
        String[] dna5 = new String[]{"AAAATCGT", "GTAACADC", "TTTGCGTT", "GGGGCCGC", "TGACCCCA", "GGAACTAT", "GGATTTTC", "GGATTATA"};
        String[] dna6 = new String[]{"ABCDEF", "GHIJKL", "MNOPQR", "STUVWX", "YZABCD", "EFGHIJ"};
        String[] dna7 = new String[]{"ACGT", "ACTG", "GCTA", "TGAC"};

        String[] dna8 = new String[]{"ACGTC", "ACTGC", "GCTAC", "TGACC","TGAGA"};

        Assert.assertTrue(service.isMutant(dna));
        Assert.assertTrue(service.isMutant(dna2));
        Assert.assertTrue(service.isMutant(dna3));
        Assert.assertTrue(service.isMutant(dna4));
        Assert.assertTrue(service.isMutant(dna5));
        Assert.assertFalse(service.isMutant(dna6));
        Assert.assertFalse(service.isMutant(dna8));*/

    }

}