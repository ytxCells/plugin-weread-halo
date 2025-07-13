package pplay.fun.service;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface BookshelfService {
    Mono<Void> save(JsonNode bookshelf);
}
