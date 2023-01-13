package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.exception.ReviewDataException;
import com.reactivespring.exception.ReviewNotFoundException;
import com.reactivespring.repository.ReviewReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.stream.Collectors;


@Component
@Slf4j
public class ReviewHandler {
    private ReviewReactiveRepository reviewReactiveRepository;
    @Autowired private Validator validator;

    public ReviewHandler(ReviewReactiveRepository reviewReactiveRepository) {
        this.reviewReactiveRepository = reviewReactiveRepository;
    }
    public Mono<ServerResponse> addReview(ServerRequest request) {
       return request.bodyToMono(Review.class)
               .doOnNext(this::validate)
               .flatMap(  reviewReactiveRepository::save)
               .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    private void validate(Review review) {
        var constraintValidations= validator.validate(review);
        log.info("constraintViolations : {}", constraintValidations);
        if (constraintValidations.size() >0){
            var errorMessage= constraintValidations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));
            throw new ReviewDataException(errorMessage);
        }
    }

    public Mono<ServerResponse> getReview(ServerRequest request) {
        var movieInfoId = request.queryParam("movieInfoId");
        Flux<Review> reviewsFlux;
        if(movieInfoId.isPresent()){
            reviewsFlux = reviewReactiveRepository.findReviewsByMovieInfoId(Long.valueOf(movieInfoId.get()));
        }else{
            reviewsFlux = reviewReactiveRepository.findAll();
        }
        return buildReviewResponse(reviewsFlux);
    }

    private Mono<ServerResponse> buildReviewResponse(Flux<Review> reviewsFlux) {
        return ServerResponse.ok().body(reviewsFlux, Review.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");
        var existingReview =  reviewReactiveRepository.findById(reviewId);
              /* THIS IS APPROACH ONE FOR NOT FINDING REVIEW FOR NOT FOUND EXCEPTION
                .switchIfEmpty(Mono.error(new ReviewNotFoundException("Review not found for the Review Id " +reviewId)));
               */
        return existingReview.flatMap(review ->
            request.bodyToMono(Review.class)
                    .map(reqReview->{
                        //we got the review passed in http request here as reqReview
                        //so we update the pre-existing one from the db
                        review.setComment(reqReview.getComment());
                        review.setRating(reqReview.getRating());
                        return review;
                    })
                    .flatMap(reviewReactiveRepository::save)
                    .flatMap(savedReview->ServerResponse.ok().bodyValue(savedReview))
                //THIS IS APPROACH TWO FOR NOT FOUND EXCEPTION,UNFORTUNATELY NO CUSTOM MESSAGE HERE
                    .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");
        var existingReview =  reviewReactiveRepository.findById(reviewId);
       return existingReview.flatMap(review -> reviewReactiveRepository.deleteById(reviewId))
               .then(ServerResponse.noContent().build());
    }
}
