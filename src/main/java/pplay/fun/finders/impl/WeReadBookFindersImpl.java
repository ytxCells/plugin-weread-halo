package pplay.fun.finders.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import pplay.fun.extension.Book;
import pplay.fun.finders.WeReadBookFinders;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.ListResult;
import run.halo.app.extension.PageRequest;
import run.halo.app.extension.PageRequestImpl;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.theme.finders.Finder;
import java.util.stream.Collectors;

import static run.halo.app.extension.ExtensionUtil.defaultSort;

@RequiredArgsConstructor
@Finder("weReadBookFinder")
public class WeReadBookFindersImpl implements WeReadBookFinders {
    private final ReactiveExtensionClient client;
    int pageNullSafe(Integer page) {
        return ObjectUtils.defaultIfNull(page, 1);
    }

    int sizeNullSafe(Integer size) {
        return ObjectUtils.defaultIfNull(size, 10);
    }
    @Override
    public Mono<ListResult<Book.BookSpec>> list(Integer page, Integer pageSize) {
        return pageWeReadBook(PageRequestImpl.of(pageNullSafe(page), sizeNullSafe(pageSize), defaultSort()));
    }

    private Mono<ListResult<Book.BookSpec>> pageWeReadBook(PageRequest pageRequest) {
        ListOptions listOptions = new ListOptions();
        return client.listBy(Book.class, listOptions, pageRequest)
            .map(bookListResult -> {
                // 将Book对象流转换为BookSpec对象流
                var bookSpecs = bookListResult.getItems().stream()
                    .map(Book::getSpec)
                    .collect(Collectors.toList());
                // 构建新的ListResult对象
                return new ListResult<>(
                    bookListResult.getPage(),
                    bookListResult.getSize(),
                    bookListResult.getTotal(),
                    bookSpecs
                );
            });
    }

}
