package pplay.fun.finders.impl;

import lombok.RequiredArgsConstructor;
import pplay.fun.extension.Review;
import pplay.fun.finders.WeReadReviewFinders;
import pplay.fun.service.ReviewService;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;
import run.halo.app.theme.finders.Finder;

@RequiredArgsConstructor
@Finder("weReadReviewFinder")
public class WeReadReviewFindersImpl implements WeReadReviewFinders {
    private final ReviewService reviewService;

    @Override
    public Mono<ListResult<Review.ReviewSpec>> list(Integer page, Integer pageSize) {
        return null;
    }
}
