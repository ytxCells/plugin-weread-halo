package pplay.fun.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;

public interface BookmarkService {

    Publisher<? extends Void> save(JsonNode bookmark);
}
