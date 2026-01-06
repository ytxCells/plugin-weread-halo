package pplay.fun.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import pplay.fun.extension.Bookmark;
import pplay.fun.service.BookmarkService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.User;
import run.halo.app.extension.ListOptions;

import run.halo.app.extension.ListResult;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.PageRequestImpl;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.index.query.Query;
import run.halo.app.extension.router.selector.FieldSelector;


import java.util.stream.Collectors;

import static run.halo.app.extension.index.query.QueryFactory.all;
import static run.halo.app.extension.index.query.QueryFactory.and;
import static run.halo.app.extension.index.query.QueryFactory.equal;

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

    @Override
    public Flux<Bookmark> getListAllByBookId(String bookId) {
        ListOptions listOptions = new ListOptions();
        Query query = all();
        if (StringUtils.isNoneBlank(bookId)) {
            query = and(query, equal("spec.bookId", bookId));
        }
        listOptions.setFieldSelector(FieldSelector.of(query));
        Sort sort = Sort.by("spec.createTime").descending();
        return client.listAll(Bookmark.class, listOptions, sort);
    }

    @Override
    public Mono<ListResult<Bookmark.BookmarkSpec>> list(PageRequestImpl of) {
        ListOptions listOptions = new ListOptions();
        return client.listBy(Bookmark.class, listOptions, of)
            .map(bookListResult -> {
                // 将Book对象流转换为BookSpec对象流
                var bookSpecs = bookListResult.getItems().stream()
                    .map(Bookmark::getSpec)
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

    @Override
    public Mono<Void> clearAll() {
        return client.list(Bookmark.class, null, null)
            .flatMap(client::delete)
            .then();
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
