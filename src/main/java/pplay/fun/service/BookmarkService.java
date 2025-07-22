package pplay.fun.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.reactivestreams.Publisher;
import pplay.fun.extension.Bookmark;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;
import run.halo.app.extension.PageRequestImpl;

public interface BookmarkService {
    Publisher<? extends Void> save(JsonNode bookmark);

    Flux<Bookmark> getListAllByBookId(String bookId);

    Mono<ListResult<Bookmark.BookmarkSpec>> list(PageRequestImpl of);
}
