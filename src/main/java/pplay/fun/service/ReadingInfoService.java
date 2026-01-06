package pplay.fun.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface ReadingInfoService {
    Publisher<? extends Void> save(JsonNode progressItem);

    Mono<Void> clearAll();
}
