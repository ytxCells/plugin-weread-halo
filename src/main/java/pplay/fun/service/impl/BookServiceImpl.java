package pplay.fun.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import pplay.fun.extension.Book;
import pplay.fun.service.BookService;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.User;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.ReactiveExtensionClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final ReactiveExtensionClient client;
    @Override
    public Publisher<? extends Void> save(JsonNode bookNode) {
        return getContextUser().flatMap(user -> {
            String userName = user.getMetadata().getName();
            String bookId = bookNode.get("bookId").asText();
            String name = userName+bookId;
                return client.fetch(Book.class, name)
                    .flatMap(book -> {
                        log.info("更新书本: {}", name);
                        return updateBook(book, bookNode);
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        log.info("创建书本: {}", name);
                        return createBook(name, bookNode);
                    }));
            })
            .then();
    }

    private Mono<Book> createBook(String name, JsonNode bookNode) {
        Book book = new Book();
        Metadata metadata = new Metadata();
        metadata.setName(name);
        book.setMetadata(metadata);
        book.populateSpecFromJson(bookNode);
        return client.create(book);
    }

    private Mono<Book> updateBook(Book book, JsonNode bookNode) {
        book.populateSpecFromJson(bookNode);
        return client.update(book);
    }

    protected Mono<User> getContextUser() {
        return ReactiveSecurityContextHolder.getContext()
            .doOnNext(ctx -> log.debug("安全上下文: {}", ctx != null ? "存在" : "空"))
            .doOnNext(ctx -> {
                if (ctx != null && ctx.getAuthentication() != null) {
                    log.debug("认证用户: {}", ctx.getAuthentication().getName());
                }
            })
            .flatMap(ctx -> {
                String username = ctx.getAuthentication().getName();
                return client.fetch(User.class, username)
                    .doOnNext(user -> log.debug("获取到用户: {}", user.getMetadata().getName()))
                    .doOnError(e -> log.error("用户查询失败", e));
            });
    }
}
