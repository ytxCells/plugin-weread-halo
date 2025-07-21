package pplay.fun.finders;

import pplay.fun.extension.Book;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;

public interface WeReadBookFinders {
    Mono<ListResult<Book.BookSpec>> list(Integer page,Integer pageSize);
}
