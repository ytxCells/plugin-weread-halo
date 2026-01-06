package pplay.fun.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import pplay.fun.extension.ReadingInfo;
import pplay.fun.service.ReadingInfoService;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.User;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.ReactiveExtensionClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadingInfoServiceImpl implements ReadingInfoService {
    private final ReactiveExtensionClient client;

    @Override
    public Publisher<? extends Void> save(JsonNode progressItem) {
        return getContextUser().flatMap(user -> {
            String userName = user.getMetadata().getName();
            String bookId = progressItem.path("bookId").asText(); // 使用path避免空指针
            String name = userName + bookId; // 构建唯一名称

            return client.fetch(ReadingInfo.class, name)
                .flatMap(readingInfo -> {
                    log.info("更新阅读信息: {}", name);
                    return updateReadingInfo(readingInfo, progressItem);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("创建阅读信息: {}", name);
                    return createReadingInfo(name, progressItem);
                }));
        }).then();
    }

    @Override
    public Mono<Void> clearAll() {
        return client.list(ReadingInfo.class, null, null)
            .flatMap(client::delete)
            .then();
    }

    private Mono<ReadingInfo> createReadingInfo(String name, JsonNode progressItem) {
        ReadingInfo readingInfo = new ReadingInfo();
        Metadata metadata = new Metadata();
        metadata.setName(name);
        readingInfo.setMetadata(metadata);
        readingInfo.populateSpecFromJson(progressItem);
        return client.create(readingInfo);
    }

    private Mono<ReadingInfo> updateReadingInfo(ReadingInfo readingInfo, JsonNode progressItem) {
        // 更新spec数据
        readingInfo.populateSpecFromJson(progressItem);
        return client.update(readingInfo);
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
