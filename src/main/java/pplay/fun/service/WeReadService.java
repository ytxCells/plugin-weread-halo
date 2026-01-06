package pplay.fun.service;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface WeReadService {

    Mono<Void> synchronizationWeRead();

    Mono<Void> clearWeRead();

}
