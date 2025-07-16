package pplay.fun.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import pplay.fun.extension.Review;
import pplay.fun.service.ReviewService;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.User;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.ReactiveExtensionClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReactiveExtensionClient client;

    @Override
    public Publisher<? extends Void> save(JsonNode jsonNode) {
        return getContextUser().flatMap(user -> {
            String userName = user.getMetadata().getName();
            String reviewId = jsonNode.path("reviewId").asText(); // 使用path避免空指针
            String name = userName + reviewId; // 构建唯一名称
            JsonNode reviewNode = jsonNode.get("review");
            return client.fetch(Review.class, name)
                .flatMap(review -> {
                    log.info("更新阅读信息: {}", name);
                    return updateReview(review, reviewNode);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("创建阅读信息: {}", name);
                    return createReview(name, reviewNode);
                }));
        }).then();
    }

    private Mono<Review> createReview(String name, JsonNode jsonNode) {
        Review Review = new Review();
        Metadata metadata = new Metadata();
        metadata.setName(name);
        Review.setMetadata(metadata);
        Review.populateSpecFromJson(jsonNode);
        return client.create(Review);
    }

    private Mono<Review> updateReview(Review Review, JsonNode jsonNode) {
        // 更新spec数据
        Review.populateSpecFromJson(jsonNode);
        return client.update(Review);
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
