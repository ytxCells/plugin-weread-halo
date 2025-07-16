package pplay.fun.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;

public interface ReadingInfoService {
    Publisher<? extends Void> save(JsonNode progressItem);
}
