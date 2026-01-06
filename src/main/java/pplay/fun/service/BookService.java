package pplay.fun.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface BookService {
    Publisher<? extends Void> save(JsonNode bookNode);

    Mono<Void> clearAll();
}
