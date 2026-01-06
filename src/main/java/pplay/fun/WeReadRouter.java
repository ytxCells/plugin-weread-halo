package pplay.fun;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pplay.fun.finders.WeReadBookFinders;
import pplay.fun.finders.WeReadBookmarkFinders;
import pplay.fun.finders.WeReadReviewFinders;
import reactor.core.publisher.Mono;
import run.halo.app.extension.ListResult;
import run.halo.app.theme.TemplateNameResolver;
import java.util.HashMap;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
@Slf4j
@RequiredArgsConstructor
@Component
public class WeReadRouter {
    private final TemplateNameResolver templateNameResolver;
    private final WeReadBookFinders weReadBookFinder;
    private final WeReadBookmarkFinders weReadBookmarkFinders;
    private final WeReadReviewFinders weReadReviewFinders;
    @Bean
    RouterFunction<ServerResponse> wereadRouterFunction() {
        return route(GET("/weread"), this::renderWeReadPage);
    }

    // 书籍列表页面
    Mono<ServerResponse> renderWeReadPage(ServerRequest request) {
        int page = request.queryParam("page").map(Integer::parseInt).orElse(1);
        int size = request.queryParam("size").map(Integer::parseInt).orElse(10);
        int type = request.queryParam("type").map(Integer::parseInt).orElse(1);
        Mono<?> dataMono;
        String title;

        switch (type) {
            case 2:
                title = "划线";
                dataMono = weReadBookmarkFinders.list(page, size);
                break;
            case 3:
                title = "笔记";
                dataMono = weReadReviewFinders.list(page, size);
                break;
            case 1:
            default:
                title = "阅读";
                dataMono = weReadBookFinder.list(page, size);
                break;
        }

        return dataMono.flatMap(bookPage ->{
            var model = new HashMap<String, Object>();
            model.put("title", title);
            model.put("items", ((ListResult<?>) bookPage).getItems());
            model.put("currentPage", ((ListResult<?>) bookPage).getPage());
            model.put("totalPages", (int) Math.ceil((double) ((ListResult<?>) bookPage).getTotal() / ((ListResult<?>) bookPage).getSize()));
            model.put("totalItems", ((ListResult<?>) bookPage).getTotal());
            model.put("pageSize", ((ListResult<?>) bookPage).getSize());
            model.put("type",type);
            return templateNameResolver.resolveTemplateNameOrDefault(request.exchange(), "weread")
                .flatMap(templateName -> ServerResponse.ok().render(templateName, model));
        });

    }
}
