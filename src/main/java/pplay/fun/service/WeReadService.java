package pplay.fun.service;

import reactor.core.publisher.Mono;

public interface WeReadService {

    Mono<Void> synchronizationWeRead();
}
