package pplay.fun.finders.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import pplay.fun.extension.Bookmark;
import pplay.fun.finders.WeReadBookmarkFinders;
import pplay.fun.service.BookmarkService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;
import run.halo.app.extension.PageRequestImpl;
import run.halo.app.theme.finders.Finder;

import static run.halo.app.extension.ExtensionUtil.defaultSort;

@RequiredArgsConstructor
@Finder("weReadBookmarkFinder")
public class WeReadBookmarkFindersImpl implements WeReadBookmarkFinders {
    private final BookmarkService bookmarkService;
    @Override
    public Flux<Bookmark> getListAllByBookId(String bookId) {
        return bookmarkService.getListAllByBookId(bookId);
    }

    @Override
    public Mono<ListResult<Bookmark.BookmarkSpec>> list(Integer page, Integer pageSize) {
        return bookmarkService.list(
            PageRequestImpl.of(pageNullSafe(page), sizeNullSafe(pageSize), defaultSort()));
    }
    int pageNullSafe(Integer page) {
        return ObjectUtils.defaultIfNull(page, 1);
    }

    int sizeNullSafe(Integer size) {
        return ObjectUtils.defaultIfNull(size, 10);
    }
}
