package pplay.fun.finders;
import pplay.fun.extension.Bookmark;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;

public interface WeReadBookmarkFinders {
    Flux<Bookmark> getListAllByBookId(String bookId);
    Mono<ListResult<Bookmark.BookmarkSpec>> list(Integer page,Integer pageSize);
}
