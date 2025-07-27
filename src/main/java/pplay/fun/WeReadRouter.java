package pplay.fun;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pplay.fun.finders.WeReadBookFinders;
import reactor.core.publisher.Mono;
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
    @Bean
    RouterFunction<ServerResponse> wereadRouterFunction() {
        return route(GET("/weread"), this::renderWeReadPage);
    }

    // 书籍列表页面
    Mono<ServerResponse> renderWeReadPage(ServerRequest request) {
        int page = request.queryParam("page").map(Integer::parseInt).orElse(1);
        int size = request.queryParam("size").map(Integer::parseInt).orElse(10);

        return weReadBookFinder.list(page, size)
            .flatMap(bookPage -> {
                var model = new HashMap<String, Object>();
                model.put("title", "阅读");
                model.put("books", bookPage.getItems());
                model.put("currentPage", bookPage.getPage());
                // 计算总页数
                model.put("totalPages", (int) Math.ceil((double) bookPage.getTotal() / bookPage.getSize()));
                model.put("totalItems", bookPage.getTotal());
                model.put("pageSize", bookPage.getSize());
                log.debug("Model data: books={}, currentPage={}, totalPages={}, totalItems={}, pageSize={}",
                    bookPage.getItems(), bookPage.getPage(), model.get("totalPages"), bookPage.getTotal(), bookPage.getSize());
                return templateNameResolver.resolveTemplateNameOrDefault(request.exchange(), "weread")
                    .flatMap(templateName -> ServerResponse.ok().render(templateName, model));
            });
    }
}
