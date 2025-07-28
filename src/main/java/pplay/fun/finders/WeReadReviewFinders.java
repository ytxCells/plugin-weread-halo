package pplay.fun.finders;
import pplay.fun.extension.Review;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;

public interface WeReadReviewFinders {
    Mono<ListResult<Review.ReviewSpec>> list(Integer page,Integer pageSize);
}
