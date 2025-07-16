package pplay.fun.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;

public interface BookService {
    Publisher<? extends Void> save(JsonNode bookNode);
}
