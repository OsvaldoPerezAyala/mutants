package com.osvaldo.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import org.wildfly.common.Assert;

import javax.inject.Inject;

@QuarkusTest
class MutantServiceTest {

    @Inject
    MutantService mutantService;

    @Test
    public void testArray() throws Exception {

        String dna = "{\"dna\":[\"ATGCGA\", \"CAGTGC\", \"TTATGT\", \"AGAAGG\", \"CCCCTA\", \"TCACTG\"]}";
        String dna2 = "{\"dna\":[\"AAAATC\", \"GTAACA\", \"TTTGCG\", \"GGGGCC\", \"TGACCC\", \"GGAACT\"]}";
        String dna3 = "{\"dna\":[\"AACATC\", \"GTAACA\", \"TTTGCG\", \"GTGGAC\", \"TGCCCC\", \"GGAACT\"]}";
        String dna4 = "{\"dna\":[\"AACATCG\", \"GTAACAG\", \"TTTGCGG\", \"GTGGACG\", \"TGCCCCT\", \"GGACCTT\", \"ATCTTTT\"]}";
        String dna5 = "{\"dna\":[\"AAAATCGT\", \"GTAACADC\", \"TTTGCGTT\", \"GGGGCCGC\", \"TGACCCCA\", \"GGAACTAT\", \"GGATTTTC\", \"GGATTATA\"]}";
        String dna6 = "{\"dna\":[\"ABCDEF\", \"GHIJKL\", \"MNOPQR\", \"STUVWX\", \"YZABCD\", \"EFGHIJ\"]}";
        String dna7 = "{\"dna\":[\"ACGT\", \"ACTG\", \"GCTA\", \"TGAC\"]}";
        String dna8 = "{\"dna\":[\"ACGTC\", \"ACTGC\", \"GCTAC\", \"TGACC\",\"TGAGA\"]}";

        Assert.assertTrue(mutantService.isMutant(dna));
        Assert.assertTrue(mutantService.isMutant(dna2));
        Assert.assertTrue(mutantService.isMutant(dna3));
        Assert.assertTrue(mutantService.isMutant(dna4));
        Assert.assertTrue(mutantService.isMutant(dna5));
        Assert.assertFalse(mutantService.isMutant(dna6));
        Assert.assertFalse(mutantService.isMutant(dna7));
        Assert.assertFalse(mutantService.isMutant(dna8));
    }

}