package pplay.fun.finders.impl;

import lombok.RequiredArgsConstructor;
import pplay.fun.finders.WeReadReviewFinders;
import pplay.fun.service.ReviewService;
import run.halo.app.theme.finders.Finder;

@RequiredArgsConstructor
@Finder("weReadReviewFinder")
public class WeReadReviewFindersImpl implements WeReadReviewFinders {
    private final ReviewService reviewService;
}
