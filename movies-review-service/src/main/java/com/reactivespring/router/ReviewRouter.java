package com.reactivespring.router;

import com.reactivespring.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {
    @Bean
    public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler reviewHandler){
        return route()
                .nest(path("/v1/reviews"), builder -> {
                    builder.POST("", reviewHandler::addReview)
                            .GET("",(request-> reviewHandler.getReview(request)))
                            .PUT("/{id}", request -> reviewHandler.updateReview(request))
                            .DELETE("/{id}",  request -> reviewHandler.deleteReview(request));

                })
                .GET("/v1/helloworld",(request-> ServerResponse.ok().bodyValue("helloworld")))
//                .POST("/v1/reviews", reviewHandler::addReview)
//                .GET("/v1/reviews",(reviewHandler:: getReview))
                .build();
    }
}
