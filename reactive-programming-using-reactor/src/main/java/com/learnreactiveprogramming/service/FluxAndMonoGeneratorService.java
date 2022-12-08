package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {
    public Flux<String> nameFlux(){
        return Flux.fromIterable(List.of("james", "spader"))
                .log();
    }
    public Mono<String> nameMono(){
        return Mono.just("John");
    }
    public Flux<String> nameFluxMap(int stringLength){
        //filter the string whose length is greater than 4
        return Flux.fromIterable(List.of("jame", "spaders"))
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength)
                .map(s->s.length() + "-"+s)
                //.map(s->s.toUpperCase())
                .log();
    }
    public Flux<String> nameFluxFlatMap(int stringLength){
        //filter the string whose length is greater than 4
        return Flux.fromIterable(List.of("jame", "spaders"))
                .filter(s->s.length() > stringLength)
                .flatMap(this::splitString)
                //.map(s->s.toUpperCase())
                .log();
    }
    public Flux<String> nameFluxFlatMapAsync(int stringLength){
        //filter the string whose length is greater than 4
        return Flux.fromIterable(List.of("jame", "spaders"))
                .filter(s->s.length() > stringLength)
                .flatMap(this::splitStringDelay)
                //.map(s->s.toUpperCase())
                .log();
    }
    public Flux<String> nameFluxFlatMapConcatMap(int stringLength){
        //filter the string whose length is greater than 4
        return Flux.fromIterable(List.of("jame", "spaders"))
                .filter(s->s.length() > stringLength)
                .concatMap(this::splitStringDelay)
                //.map(s->s.toUpperCase())
                .log();
    }
    public Mono<String> namesMonoFilter(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength)
                .log();
    }
    public Mono<List<String>> namesMonoFilterFlatMap(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength)
                .flatMap(this::splitStringMono) //Mono<List of A, L,E,X>
                .log();
    }
    public Flux<String> namesMonoFilterFlatMapMany(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength)
                .flatMapMany(this::splitString) //Mono<List of A, L,E,X>
                .log();
    }
    public Flux<String> nameFluxTransform(int stringLength){
        //filter the string whose length is greater than 4
        Function<Flux<String>, Flux<String>> filterMap = name-> name.map(String::toUpperCase)
                .filter(s->s.length() > stringLength);

        return Flux.fromIterable(List.of("jame", "spaders"))
                .transform(filterMap)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }
    public Flux<String> nameFluxTransformSwitchIfEmpty(int stringLength){
        //filter the string whose length is greater than 4
        Function<Flux<String>, Flux<String>> filterMap = name->
                name.map(String::toUpperCase)
                .filter(s->s.length() > stringLength).flatMap(this::splitString);
        var defaultFlux = Flux.just("default").transform(filterMap); //"D","E","F","A","U","L","T"

        return Flux.fromIterable(List.of("jame", "spaders"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }
    public Flux<String> exploreConcat(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        return Flux.concat(abcFlux,defFlux).log();

    }
    public Flux<String> exploreConcatWith(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        return abcFlux.concatWith(defFlux).log();

    }
    public Flux<String> exploreConcatWithMono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.concatWith(bMono).log();

    }
    public Flux<String> exploreMerge(){
        var abcFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(125));
        return Flux.merge(abcFlux,defFlux).log();

    }
    public Flux<String> exploreMergeWith(){
        var abcFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(125));
        return abcFlux.mergeWith(defFlux).log();

    }
    public Flux<String> exploreMergeWithSequential(){
        var abcFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(125));
        return Flux.mergeSequential(abcFlux,defFlux).log();

    }
    public Flux<String> exploreZip(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        return Flux.zip(abcFlux,defFlux,(first,second) -> first+second).log(); //AD, BE, CF

    }
    public Flux<String> exploreZipTuple(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        var multiFlux1 = Flux.just("1","2","3");
        var multiFlux2= Flux.just("4","5","6");
        return Flux.zip(abcFlux,defFlux,multiFlux1,multiFlux2)
                .map(t4-> t4.getT1()+t4.getT2()+t4.getT3()+t4.getT4())
                .log();
    }
    public Flux<String> exploreZipWith(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        return abcFlux.zipWith(defFlux, (first, second)-> first+second).log();
    }
    public Mono<String> exploreZipWithMono(){
        var abcFlux = Mono.just("A");
        var defFlux = Mono.just("B");
        return abcFlux.zipWith(defFlux)
                .map(t2->t2.getT1()+t2.getT2())
                .log();
    }
    private Mono<List<String>> splitStringMono(String s){
      var charArray =   s.split("");
      var charList = List.of(charArray);
      return Mono.just(charList);
    }
    //ALEX -> Flux(A,L,E,X)
    public Flux<String> splitString(String name){
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }
    public Flux<String> splitStringDelay(String name){
        var charArray = name.split("");
        var delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }
    public static void main(String [] args){
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.nameFlux()
                .subscribe(name->{
                    System.out.println("Name is : "+name);
                });
        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name->{
                    System.out.println("Mono Name is : "+name);
                });
    }
}
