package pplay.fun.finders.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import pplay.fun.extension.Book;
import pplay.fun.extension.Review;
import pplay.fun.finders.WeReadReviewFinders;
import pplay.fun.service.ReviewService;
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
@Finder("weReadReviewFinder")
public class WeReadReviewFindersImpl implements WeReadReviewFinders {
    private final ReactiveExtensionClient client;

    int pageNullSafe(Integer page) {
        return ObjectUtils.defaultIfNull(page, 1);
    }

    int sizeNullSafe(Integer size) {
        return ObjectUtils.defaultIfNull(size, 10);
    }
    @Override
    public Mono<ListResult<Review.ReviewSpec>> list(Integer page, Integer pageSize) {
        return pageWeReview(
            PageRequestImpl.of(pageNullSafe(page), sizeNullSafe(pageSize), defaultSort()));
    }

    private Mono<ListResult<Review.ReviewSpec>> pageWeReview(PageRequest pageRequest) {
        ListOptions listOptions = new ListOptions();
        return client.listBy(Review.class, listOptions, pageRequest)
            .map(reviewListResult -> {
                var reviewSpecs = reviewListResult.getItems().stream()
                    .map(Review::getSpec)
                    .collect(Collectors.toList());
                // 构建新的ListResult对象
                return new ListResult<>(
                    reviewListResult.getPage(),
                    reviewListResult.getSize(),
                    reviewListResult.getTotal(),
                    reviewSpecs
                );
            });
    }
}
