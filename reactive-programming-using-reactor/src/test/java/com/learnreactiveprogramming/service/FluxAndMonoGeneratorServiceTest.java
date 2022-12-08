package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;


class FluxAndMonoGeneratorServiceTest {
FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void nameFlux() {
        //given
        //when
        var namesFlux =fluxAndMonoGeneratorService.nameFlux();
        //then
        StepVerifier.create(namesFlux)
                .expectNext("james", "spader")
                //we can use count too like .expectNextCount(2)
                .expectNextCount(0) //0 coz we have already taken the values
                .verifyComplete();
    }

    @Test
    void nameFluxMap() {
        int stringLength =4;
        var namesFlux = fluxAndMonoGeneratorService.nameFluxMap(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("7-SPADERS")
                .verifyComplete();
    }

    @Test
    void nameFluxFlatMap() {
        int stringLength =3;
      var namesFlux=  fluxAndMonoGeneratorService.nameFluxFlatMap(stringLength);
      StepVerifier.create(namesFlux)
              .expectNext("j","a","m","e","s","p","a","d","e","r","s")
              .verifyComplete();
    }

    @Test
    void nameFluxFlatMapAsync() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.nameFluxFlatMapAsync(stringLength);
        StepVerifier.create(namesFlux)
                //.expectNext("j","a","m","e","s","p","a","d","e","r","s")
                .expectNextCount(11)
                .verifyComplete();
    }

    @Test
    void nameFluxFlatMapConcatMap() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.nameFluxFlatMapAsync(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("j","a","m","e","s","p","a","d","e","r","s")
                .verifyComplete();
    }

    @Test
    void namesMonoFilterFlatMap() {
        //given
        int stringLength = 3;
        //when
        var value =fluxAndMonoGeneratorService.namesMonoFilterFlatMap(stringLength);
        //then
        StepVerifier.create(value)
                .expectNext(List.of("A", "L","E","X"))
                .verifyComplete();

    }
    @Test
    void namesMonoFilterFlatMapMany() {
        //given
        int stringLength = 3;
        //when
        var value =fluxAndMonoGeneratorService.namesMonoFilterFlatMapMany(stringLength);
        //then
        StepVerifier.create(value)
                .expectNext("A", "L","E","X")
                .verifyComplete();

    }

    @Test
    void nameFluxTransform() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.nameFluxTransform(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("J","A","M","E","S","P","A","D","E","R","S")
                .verifyComplete();
    }
    @Test
    void nameFluxTransformEmpty() {
        int stringLength = 7;
        var namesFlux = fluxAndMonoGeneratorService.nameFluxTransform(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }
    @Test
    void nameFluxTransformEmptySwitchIfEmpty() {
        int stringLength = 7;
        var namesFlux = fluxAndMonoGeneratorService.nameFluxTransformSwitchIfEmpty(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void exploreConcat() {
        var concatFlux = fluxAndMonoGeneratorService.exploreConcat();
        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreConcatWith() {
        var concatFlux = fluxAndMonoGeneratorService.exploreConcatWith();
        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreConcatWithMono() {
        var concatWithMono = fluxAndMonoGeneratorService.exploreConcatWithMono();
        StepVerifier.create(concatWithMono)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void exploreMerge() {
        var value = fluxAndMonoGeneratorService.exploreMerge();
        StepVerifier.create(value)
                .expectNext("A", "D","B","E","C","F")
                .verifyComplete();

    }

    @Test
    void exploreMergeWith() {
        var value = fluxAndMonoGeneratorService.exploreMergeWith();
        StepVerifier.create(value)
                .expectNext("A", "D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void exploreMergeWithSequential() {
        var value = fluxAndMonoGeneratorService.exploreMergeWithSequential();
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreZip() {
        var value = fluxAndMonoGeneratorService.exploreZip();
        StepVerifier.create(value)
                .expectNext("AD", "BE","CF")
                .verifyComplete();
    }

    @Test
    void exploreZipTuple() {
        var value = fluxAndMonoGeneratorService.exploreZipTuple();
        StepVerifier.create(value)
                .expectNext("AD14", "BE25","CF36")
                .verifyComplete();
    }
}