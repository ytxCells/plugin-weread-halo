package pplay.fun.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pplay.fun.model.Bookshelf;
import pplay.fun.service.BookshelfService;
import pplay.fun.util.UserUtils;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.User;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.ReactiveExtensionClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookshelfServiceImpl implements BookshelfService {
    private final ReactiveExtensionClient client;

    @Override
    public Mono<Void> save(JsonNode jsonNode) {
        return getContextUser()
            .flatMap(user -> {
                String userName = user.getMetadata().getName();
                return client.fetch(Bookshelf.class, userName)
                    .flatMap(existing -> {
                        log.info("更新用户书架: {}", userName);
                        return updateBookShelf(existing, jsonNode);
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        log.info("创建用户书架: {}", userName);
                        return createBookShelf(userName, jsonNode);
                    }));
            })
            .then(); // 转换为 Mono<Void>
    }

    private Mono<Bookshelf> createBookShelf(String name, JsonNode jsonNode) {
        Bookshelf bookShelf = new Bookshelf();
        Metadata metadata = new Metadata();
        metadata.setName(name);
        bookShelf.setMetadata(metadata);
        bookShelf.populateSpecFromJson(jsonNode);
        return client.create(bookShelf);
    }

    private Mono<Bookshelf> updateBookShelf(Bookshelf bookShelf, JsonNode jsonNode) {
        bookShelf.populateSpecFromJson(jsonNode);
        return client.update(bookShelf);
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
