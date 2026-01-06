package pplay.fun.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface ReviewService {
    Publisher<? extends Void> save(JsonNode review);

    Mono<Void> clearAll();

}
