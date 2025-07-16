package pplay.fun.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import pplay.fun.extension.Bookmark;
import pplay.fun.service.BookmarkService;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.User;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.ReactiveExtensionClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final ReactiveExtensionClient client;

    @Override
    public Publisher<? extends Void> save(JsonNode bookmarkJsonNode) {
        return getContextUser().flatMap(user -> {
            String userName = user.getMetadata().getName();
            String bookmarkId = bookmarkJsonNode.path("bookmarkId").asText(); // 使用path避免空指针
            String name = userName + bookmarkId; // 构建唯一名称

            return client.fetch(Bookmark.class, name)
                .flatMap(bookmark -> {
                    log.info("更新阅读信息: {}", name);
                    return updateBookmark(bookmark, bookmarkJsonNode);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("创建阅读信息: {}", name);
                    return createBookmark(name, bookmarkJsonNode);
                }));
        }).then();
    }
    private Mono<Bookmark> createBookmark(String name, JsonNode progressItem) {
        Bookmark Bookmark = new Bookmark();
        Metadata metadata = new Metadata();
        metadata.setName(name);
        Bookmark.setMetadata(metadata);
        Bookmark.populateSpecFromJson(progressItem);
        return client.create(Bookmark);
    }

    private Mono<Bookmark> updateBookmark(Bookmark Bookmark, JsonNode progressItem) {
        // 更新spec数据
        Bookmark.populateSpecFromJson(progressItem);
        return client.update(Bookmark);
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
